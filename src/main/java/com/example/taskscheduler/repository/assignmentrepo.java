package com.example.taskscheduler.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskscheduler.domain.Assignment;
public interface assignmentrepo extends JpaRepository<Assignment, Long>{
    List<Assignment> findByStrategyUsed(String strategyUsed);
}
