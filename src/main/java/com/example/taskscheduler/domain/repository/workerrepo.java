package com.example.taskscheduler.domain.repository;
import com.example.taskscheduler.domain.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.taskscheduler.domain.workerstatus;
import java.util.List;
public interface workerrepo extends JpaRepository<Worker, Long>{
    List<Worker> findByStatus(workerstatus status);
}
