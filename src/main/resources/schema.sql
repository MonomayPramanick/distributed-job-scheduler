CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    priority INT,
    retry_count INT DEFAULT 0,
    max_retries INT,
    scheduled_time DATETIME NOT NULL,
    created_at DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS job_executions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    job_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    error_message TEXT,
    CONSTRAINT fk_job_execution
        FOREIGN KEY (job_id) REFERENCES jobs(id)
);
