package com.ipap;

import java.util.stream.IntStream;

public class VirtualThread {
    public static void main(String[] args) throws InterruptedException {

        // A Platform thread
        // Thread.ofPlatform().start(() -> System.out.println("Platform Thread : " + Thread.currentThread()));

        System.out.println("------------------------------");

        // A Virtual thread
        // Thread.ofVirtual().start(() -> System.out.println("Virtual Thread : " + Thread.currentThread()));

        // Start time
        var start = System.currentTimeMillis();
        // Number of virtual threads to spawn
        var totalThreads = 100000; // 1M
        var threads = IntStream.range(0,totalThreads)
                .mapToObj(
                        // Use Thread.ofPlatform() to see the difference in Thread spawn time
                        index -> Thread.ofVirtual().unstarted(() -> {
                            // Check the initial Platform thread where the Virtual Thread was assigned
                            if (index == 0) {
                                System.out.println(Thread.currentThread());
                            }
                            try {
                                // While sleep occurs, the Virtual thread is released from the initial Platform thread
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            // Check the latest Platform thread where the Virtual Thread was assigned
                            if (index == 0) {
                                System.out.println(Thread.currentThread());
                            }
                        })
                ).toList();

        // Run threads
        threads.forEach(Thread::start);

        // Join threads
        for (Thread thread : threads) {
            thread.join();
        }

        // End time
        var end = System.currentTimeMillis();

        // Result
        System.out.println("Milliseconds needed to launch " + totalThreads + " virtual-threads " + (end - start) + "ms");
    }
}