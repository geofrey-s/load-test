package com.mparticle.test;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;
import java.util.stream.IntStream;

public class MetricsCollectorTest {

    @Test
    @Ignore
    public void should_summarize_test_result_on_console() {
        MetricsCollector.cliOptions = CliArgumentsParser.from(new String[]{});

        IntStream.range(0, 5).forEach((__) -> {
            long start = System.nanoTime();
            long duration = new Random().nextInt(100000000);
            int status = new Random().nextBoolean() ? 200 : 400;

            MetricsCollector.metrics.add(RequestResponseSummary.create(start, duration, status));
        });

        MetricsCollector.summarize();
    }
}