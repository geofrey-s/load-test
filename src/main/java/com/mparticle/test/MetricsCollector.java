package com.mparticle.test;

import com.mparticle.test.CliArgumentsParser.CliOption;
import kong.unirest.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.mparticle.test.CliArgumentsParser.CliOption.TARGET_RPS;
import static com.mparticle.test.Constants.*;
import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class MetricsCollector {
    public static CliArgumentsParser cliOptions;

    public static AtomicLong currentRps = new AtomicLong(0);
    public static List<RequestResponseSummary> metrics = new ArrayList<>();

    public static MetricContext profiler(HttpRequestSummary requestSummary) {
        long start = nanoTime();
        return (responseSummary, exception) -> MetricsCollector.log(start, responseSummary);
    }

    public static void log(long start, HttpResponseSummary responseSummary) {
        CompletableFuture.runAsync(() -> {
            RequestResponseSummary summary = RequestResponseSummary.create(start, nanoTime() - start, responseSummary.getStatus());
            MetricsCollector.metrics.add(summary);
        });
    }

    public static void collect() {
        String targetRps = MetricsCollector.cliOptions.getString(TARGET_RPS);
        String host = MetricsCollector.cliOptions.getString(CliOption.HOST);
        String path = MetricsCollector.cliOptions.getString(CliOption.PATH);

        CompletableFuture.runAsync(() -> {
            long seconds = 1;
            System.out.println("# Running Load Test ...");
            System.out.printf("# Server Url: %s/%s\n", host, path);
            System.out.println("Requests Per Second: Target | Actual");

            while (true) {
                try {
                    long actualRps = MetricsCollector.metrics.size() / seconds++;
                    MetricsCollector.currentRps.set(actualRps);
                    System.out.printf("%27s |%7s\r", targetRps, actualRps);

                    Thread.sleep(ONE_SECOND);
                } catch (InterruptedException e) {
                    MetricsCollector.summarize();
                }
            }
        });
    }

    public static void summarize() {
        Unirest.shutDown();

        CompletableFuture.runAsync(() -> {
            LongSummaryStatistics durationStats = MetricsCollector.metrics
                    .stream()
                    .mapToLong(log -> log.duration)
                    .summaryStatistics();

            int[] statusSummary = new int[]{0, 0};
            for (RequestResponseSummary log : MetricsCollector.metrics) {
                int __ = log.isSuccessful ? statusSummary[STATUS_SUCCESSFUL_INDEX]++ : statusSummary[STATUS_FAIL_INDEX]++;
            }

            System.out.println("\nSummary Stats:");
            System.out.printf("Total requests: %6s | Successful: %6s | Failed: %6s | rps: %6s | avg. latency: %6sms | min. latency: %6sms | max. latency: %6sms",
                    durationStats.getCount(),
                    statusSummary[STATUS_SUCCESSFUL_INDEX],
                    statusSummary[STATUS_FAIL_INDEX],
                    MetricsCollector.currentRps.get(),
                    TimeUnit.MILLISECONDS.convert((long) durationStats.getAverage(), NANOSECONDS),
                    TimeUnit.MILLISECONDS.convert(durationStats.getMin(), NANOSECONDS),
                    TimeUnit.MILLISECONDS.convert(durationStats.getMax(), NANOSECONDS)
            );
        }).join();

        int targetRps = MetricsCollector.cliOptions.getInteger(TARGET_RPS);
        long actualRps = MetricsCollector.currentRps.get();

        System.out.printf("\nLoad test completed: Passed: %s\n", actualRps >= targetRps);
    }

}
