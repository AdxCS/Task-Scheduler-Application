package com.example.taskscheduler.domain.repository;
import com.example.taskscheduler.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface assignmentrepo extends JpaRepository<Assignment, Long>{
    List<Assignment> findStratergyUsed(String stratergyUsed);
}
