package com.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
	    name = "jobs",
	    indexes = {
	        @Index(name = "idx_job_status", columnList = "status"),
	        @Index(name = "idx_job_scheduled_time", columnList = "scheduled_time")
	    }
	)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Job {

		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Long id;
		
		private String name;
		
		private JobType type;
		
		@Enumerated(EnumType.STRING)
		private JobStatus status;
		
		private int priority;
		
		private int retryCount;
		
		private int maxRetries;
		
		@Column(name = "scheduled_time")
		private LocalDateTime scheduledTime;
		
		private LocalDateTime createdAt;
		
		
}
