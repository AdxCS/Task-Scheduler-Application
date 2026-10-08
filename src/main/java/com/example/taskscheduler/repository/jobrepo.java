package com.example.taskscheduler.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskscheduler.domain.Job;
import com.example.taskscheduler.domain.jobstatus;
public interface jobrepo extends JpaRepository<Job, Long>{
    List<Job> findByStatus(jobstatus status);
    Optional<Job> findByIdempotencyKey(String IdempotencyKey);
}