# Distributed Job Scheduler

A production-style distributed job scheduler built using Spring Boot.

## 🚀 Features
- Time-based job scheduling
- Background job execution
- Blocking queue with worker threads
- Redis distributed locking (SETNX + TTL)
- Retry mechanism with exponential backoff
- Execution history persistence
- MySQL-based job storage

## 🏗 Architecture

Client  
→ REST API  
→ MySQL (Jobs Table)  
→ Scheduler (polls due jobs)  
→ Blocking Queue  
→ Worker Threads (ExecutorService)  
→ Redis Lock  
→ Job Execution  

## 🔄 Job Lifecycle

PENDING → RUNNING → SUCCESS  
PENDING → RUNNING → FAILED → RETRY → PENDING  
After max retries → FAILED

## 🛠 Tech Stack
- Java
- Spring Boot
- MySQL
- Redis
- JPA / Hibernate
- ExecutorService

## 🧪 How It Works
1. Jobs are created with a scheduled time and stored in MySQL.
2. Scheduler polls due jobs every 5 seconds.
3. Jobs are pushed into an in-memory blocking queue.
4. Worker threads execute jobs concurrently.
5. Redis ensures at-most-once execution.
6. Failures are retried using exponential backoff.

## 📌 Example Use Cases
- Email sending
- Report generation
- Notification processing
- Background workflows

## ▶ Run Locally
1. Start MySQL and Redis
2. Configure `application.yml`
3. Run Spring Boot application
