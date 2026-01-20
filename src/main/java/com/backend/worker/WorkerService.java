package com.backend.worker;

import com.backend.entity.*;
import com.backend.repository.JobExecutionRepository;
import com.backend.repository.JobRepository;
import com.backend.service.RedisLockService;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Service
public class WorkerService {

    private final JobRepository jobRepository;
    private final JobExecutionRepository executionRepository;
    private final RedisLockService redisLockService;

    // consumes queue
    private final ExecutorService consumerPool =
            Executors.newSingleThreadExecutor();

    // executes jobs
    private final ExecutorService workerPool =
            Executors.newFixedThreadPool(3);

    public WorkerService(JobRepository jobRepository,
                         JobExecutionRepository executionRepository,
                         RedisLockService redisLockService) {
        this.jobRepository = jobRepository;
        this.executionRepository = executionRepository;
        this.redisLockService = redisLockService;
    }

    @PostConstruct
    public void start() {
        consumerPool.submit(this::consume);
    }

    private void consume() {
        while (true) {
            try {
                Long jobId = JobQueue.take();

                // ✅ correct usage
                workerPool.submit(() -> executeJob(jobId));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void executeJob(Long jobId) {

        String lockKey = "job:lock:" + jobId;
        if (!redisLockService.acquire(lockKey, 30)) {
            return;
        }

        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            redisLockService.release(lockKey);
            return;
        }

        System.out.println("[WORKER] Executing job " + job.getId());

        JobExecution exec = new JobExecution();
        exec.setJob(job);
        exec.setStartTime(LocalDateTime.now());
        exec.setStatus(JobStatus.RUNNING);

        try {
            Thread.sleep(2000);

            if (Math.random() < 0.6) {
                throw new RuntimeException("Simulated failure");
            }

            job.setStatus(JobStatus.SUCCESS);
            exec.setStatus(JobStatus.SUCCESS);

            System.out.println("[WORKER] Job success " + job.getId());

        } catch (Exception e) {

            System.out.println("[WORKER] Job failed " + job.getId());

            handleRetry(job, exec, e.getMessage());
        }

        exec.setEndTime(LocalDateTime.now());

        executionRepository.save(exec);
        jobRepository.save(job);

        redisLockService.release(lockKey);
    }

    private void handleRetry(Job job, JobExecution exec, String error) {

        exec.setStatus(JobStatus.FAILED);
        exec.setErrorMessage(error);

        job.setRetryCount(job.getRetryCount() + 1);

        if (job.getRetryCount() <= job.getMaxRetries()) {

            int delaySeconds =
                    (int) Math.pow(2, job.getRetryCount()) * 10;

            job.setScheduledTime(
                    LocalDateTime.now().plusSeconds(delaySeconds)
            );

            job.setStatus(JobStatus.PENDING);

            System.out.println(
                    "[WORKER] Retry job " + job.getId() +
                    " after " + delaySeconds + " seconds"
            );

        } else {
            job.setStatus(JobStatus.FAILED);
            System.out.println(
                    "[WORKER] Job permanently failed " + job.getId()
            );
        }
    }
}
