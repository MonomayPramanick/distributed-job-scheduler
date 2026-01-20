package com.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.dto.CreateJobRequest;
import com.backend.entity.Job;
import com.backend.entity.JobExecution;
import com.backend.entity.JobStatus;
import com.backend.entity.JobType;
import com.backend.repository.JobExecutionRepository;
import com.backend.repository.JobRepository;

@Service
public class JobService {
	
	private final JobRepository jobRepository;
	private final JobExecutionRepository jobExecutionRepository;
	public JobService(JobRepository jobRepository,
            JobExecutionRepository jobExecutionRepository) {
			this.jobRepository = jobRepository;
			this.jobExecutionRepository = jobExecutionRepository;
	}
	
	public Job createJob(CreateJobRequest req) {
		
		Job job=new Job();
		
		job.setName(req.getName());
        job.setType(JobType.valueOf(req.getType()));
        job.setPriority(req.getPriority());
        job.setScheduledTime(req.getScheduledTime());
        job.setMaxRetries(req.getMaxRetries());

        job.setRetryCount(0);
        job.setStatus(JobStatus.PENDING);
        job.setCreatedAt(LocalDateTime.now());

        return jobRepository.save(job);

	}
	
	public List<Job> getJobs(JobStatus status) {
        if (status == null) {
            return jobRepository.findAll();
        }
        return jobRepository.findAll()
                .stream()
                .filter(j -> j.getStatus() == status)
                .toList();
    }
	
	public void cancelJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus(JobStatus.FAILED);
        jobRepository.save(job);
    }
	 public List<JobExecution> getJobExecutions(Long jobId) {
	        Job job = jobRepository.findById(jobId)
	                .orElseThrow(() -> new RuntimeException("Job not found"));

	        return jobExecutionRepository.findAll()
	                .stream()
	                .filter(e -> e.getJob().getId().equals(jobId))
	                .toList();
	    }
	

}
