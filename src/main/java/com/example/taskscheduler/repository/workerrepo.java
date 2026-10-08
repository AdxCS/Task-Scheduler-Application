package com.example.taskscheduler.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskscheduler.domain.Worker;
import com.example.taskscheduler.domain.workerstatus;
public interface workerrepo extends JpaRepository<Worker, Long>{
    List<Worker> findByStatus(workerstatus status);
}