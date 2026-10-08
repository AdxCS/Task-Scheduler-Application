package com.example.taskscheduler.domain.repository;
import com.example.taskscheduler.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.taskscheduler.domain.jobstatus;
import java.util.List;
import java.util.Optional;
public interface jobrepo extends JpaRepository<Job, Long> {
    List<Job> findByStatus(jobstatus status);
    Optional<Job> findByIdempotencyKey(String IdempotencyKey);
    

    
}
