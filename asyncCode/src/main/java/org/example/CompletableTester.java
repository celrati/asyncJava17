package main.java.org.example;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class CompletableTester {
    public static record Quotation(String name, int amount) {}
    public static void run() {
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

        List<Quotation> quotiationsList = new ArrayList<>();
        for(CompletableFuture<Quotation> completableFuture : listCompletableFuture) {
            Quotation quotation = completableFuture.join();
            quotiationsList.add(quotation);
        }


        var bestQuotation = quotiationsList.stream()
                .min(Comparator.comparing(Quotation::amount))
                .orElseThrow();

        Instant end = Instant.now();
        Duration duration = Duration.between(begin, end);
        System.out.println("best quotation " + bestQuotation + "("+duration.toMillis() +")");
    }





    public static void main(String[] args) {
    run();
    }
}
