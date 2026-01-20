package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.entity.JobExecution;

public interface JobExecutionRepository extends JpaRepository<JobExecution,Long> {

}
