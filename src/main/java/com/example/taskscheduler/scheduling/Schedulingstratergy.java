package com.example.taskscheduler.scheduling;
import java.util.List;
import java.util.Optional;

import com.example.taskscheduler.domain.Job;
import com.example.taskscheduler.domain.Worker;
public interface Schedulingstratergy {
    Optional<Worker> selectWorker(Job job, List<Worker> candidates);
    String name();
    
}
