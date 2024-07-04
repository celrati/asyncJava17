package main.java.org.example;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        run();
    }

    public static void run() throws ExecutionException, InterruptedException {
        Random random = new Random();

        Callable<String> fetchQuotation1 = () -> {
            Thread.sleep(random.nextInt(80, 120));
            return "1";
        };
        Callable<String> fetchQuotation2 = () -> {
            Thread.sleep(random.nextInt(80, 120));
            return "0";
        };
        Callable<String> fetchQuotation3 = () -> {
            Thread.sleep(random.nextInt(80, 120));
            return "3";
        };


        var list = List.of(fetchQuotation1, fetchQuotation2, fetchQuotation3);
        var executor = Executors.newFixedThreadPool(4);
        List<Future<String>> futureList = new ArrayList<>();

        for(Callable<String> callable : list) {
            Future<String> future =  executor.submit(callable);
            futureList.add(future);
        }

        List<String> quotationList = new ArrayList<>();
        for(Future<String> future : futureList) {
            String quotation = future.get();
            quotationList.add(quotation);
        }

        String  quo = quotationList.stream()
                .min(Comparator.naturalOrder())
                .orElseThrow();

        Instant begin = Instant.now();

        System.out.println(quo);
        Instant end = Instant.now();
        Duration duration = Duration.between(begin, end);

    }

    public static record Quotation(String name, int amount) {}
}