package com.webcrawler;

import java.util.concurrent.TimeUnit;

public class Benchmark {
    private final String testName;
    private long startTime;
    private long endTime;

    public Benchmark(String testName) {
        this.testName = testName;
    }

    // Start the timer
    public void start() {
        startTime = System.nanoTime();
    }

    // Stop the timer
    public void stop() {
        endTime = System.nanoTime();
    }

    // Print elapsed time
    public void printResults() {
        long elapsedTime = endTime - startTime;
        long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(elapsedTime);
        System.out.println("Benchmark: " + testName);
        System.out.println("Elapsed Time: " + elapsedMillis + " ms");
    }

    // Get elapsed time in milliseconds
    public long getElapsedMillis() {
        return TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    }
}
