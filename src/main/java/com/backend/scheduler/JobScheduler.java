package com.backend.scheduler;

import com.backend.entity.Job;
import com.backend.entity.JobStatus;
import com.backend.repository.JobRepository;
import com.backend.worker.JobQueue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class JobScheduler {

    private final JobRepository jobRepository;

    public JobScheduler(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Scheduled(fixedDelay = 5000)
    public void pollJobs() {
    		System.out.println("I am in poll");
        List<Job> dueJobs =
                jobRepository.findByStatusAndScheduledTimeBefore(
                        JobStatus.PENDING,
                        LocalDateTime.now()
                );
        System.out.println("I am in poll");
        for (Job job : dueJobs) {

            int updated =
                    jobRepository.updateJobStatus(
                            job.getId(),
                            JobStatus.PENDING,
                            JobStatus.RUNNING
                    );
          

            // only one scheduler wins
            if (updated == 1) {
                JobQueue.add(job.getId());
                System.out.println("Job picked: " + job.getId());
            }
        }
    }
}
