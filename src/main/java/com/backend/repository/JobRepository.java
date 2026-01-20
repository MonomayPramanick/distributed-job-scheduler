package com.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.entity.Job;
import com.backend.entity.JobStatus;

import jakarta.transaction.Transactional;

public interface JobRepository extends JpaRepository<Job,Long>{
	
	List<Job> findByStatusAndScheduledTimeBefore(
            JobStatus status,
            LocalDateTime time
    );
	@Modifying
    @Transactional
    @Query("""
        update Job j
        set j.status = :newStatus
        where j.id = :jobId
          and j.status = :oldStatus
    """)
    int updateJobStatus(
            @Param("jobId") Long jobId,
            @Param("oldStatus") JobStatus oldStatus,
            @Param("newStatus") JobStatus newStatus
    );
}
