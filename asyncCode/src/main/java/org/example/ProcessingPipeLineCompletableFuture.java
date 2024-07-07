package main.java.org.example;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Supplier;

public class ProcessingPipeLineCompletableFuture {
    public static record Quotation(String name, int amount) {}
    public static void run() throws InterruptedException {
        Random random = new Random();

        Supplier<Quotation> fetchQuotation1 = () -> {
            try {
                Thread.sleep(random.nextInt(80, 120));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new Quotation("server 1", random.nextInt(40, 60));
        };
        Supplier<Quotation> fetchQuotation2 = () -> {
            try {
                Thread.sleep(random.nextInt(80, 120));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new Quotation("server 2", random.nextInt(30, 70));
        };
        Supplier<Quotation> fetchQuotation3 = () -> {
            try {
                Thread.sleep(random.nextInt(80, 120));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new Quotation("server 3", random.nextInt(40, 80));
        };

        var list = List.of(fetchQuotation1, fetchQuotation2, fetchQuotation3);

        Instant begin = Instant.now();

        List<CompletableFuture> listCompletableFuture = new ArrayList<>();
        for(Supplier<Quotation> quotationSupplier : list) {
            CompletableFuture<Quotation> completableFuture = CompletableFuture.supplyAsync(quotationSupplier);
            listCompletableFuture.add(completableFuture);
        }

        Collection<Quotation> quotiationsList = new ConcurrentLinkedDeque<>();
        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        for(CompletableFuture<Quotation> completableFuture : listCompletableFuture) {
            CompletableFuture<Void> completableFuture1 =  completableFuture.thenAccept(System.out::println);
            completableFutures.add(completableFuture1);
            completableFuture.thenAccept(quotiationsList::add);
        }


        //var bestQuotation = quotiationsList.stream()
        //        .min(Comparator.comparing(CompletableTester.Quotation::amount))
        //        .orElseThrow();

        Instant end = Instant.now();
        Duration duration = Duration.between(begin, end);
        //System.out.println("best quotation " + bestQuotation + "("+duration.toMillis() +")");
        System.out.println(quotiationsList);
        for(CompletableFuture<Void> completableFuture : completableFutures) {
            completableFuture.join();
        }
        System.out.println(quotiationsList);
        // Thread.sleep(1000);
    }





    public static void main(String[] args) throws InterruptedException {
        run();
    }
}
