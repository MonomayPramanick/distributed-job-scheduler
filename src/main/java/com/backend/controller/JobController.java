package com.backend.controller;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.backend.dto.CreateJobRequest;
import com.backend.entity.Job;
import com.backend.entity.JobExecution;
import com.backend.entity.JobStatus;
import com.backend.service.JobService;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    
    @PostMapping
    public Job createJob(@Valid @RequestBody CreateJobRequest request) {
        return jobService.createJob(request);
    }
    
    @GetMapping
    public List<Job> getJobs(
            @RequestParam(required = false) JobStatus status) {
        return jobService.getJobs(status);
    }
    
    @DeleteMapping("/{jobId}")
    public String cancelJob(@PathVariable Long jobId) {
        jobService.cancelJob(jobId);
        return "Job cancelled";
    }
    @GetMapping("/{jobId}/executions")
    public List<JobExecution> getExecutions(@PathVariable Long jobId) {
        return jobService.getJobExecutions(jobId);
    }
}