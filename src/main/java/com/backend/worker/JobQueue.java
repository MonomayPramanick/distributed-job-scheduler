package com.backend.worker;

import java.util.concurrent.PriorityBlockingQueue;


public class JobQueue {

	private static final PriorityBlockingQueue<Long> queue =
            new PriorityBlockingQueue<>();

    public static void add(Long jobId) {
        queue.offer(jobId);
    }

    public static Long take() throws InterruptedException {
        return queue.take();
    }
}
