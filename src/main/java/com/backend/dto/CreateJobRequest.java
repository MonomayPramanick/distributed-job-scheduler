package com.backend.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobRequest {

	@NotBlank
	private String name;
	
	@NotNull
	private String type;
	
	@NotNull
	private Integer priority;
	
	@NotNull
    private LocalDateTime scheduledTime;

    @NotNull
    private Integer maxRetries;

	
}
