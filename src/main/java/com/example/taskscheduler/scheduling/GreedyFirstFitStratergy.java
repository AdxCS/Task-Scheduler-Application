package com.example.taskscheduler.scheduling;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.taskscheduler.domain.Job;
import com.example.taskscheduler.domain.Worker;
@Component 
public class GreedyFirstFitStratergy implements Schedulingstratergy {
    @Override
    public Optional<Worker> selectWorker(Job job, List<Worker> candidates) {
        return candidates.stream()
                .filter(w -> w.hascapacityfor(job.getCpuRequired(), job.getMemoryRequired()))
                .min(Comparator.comparingInt(w -> w.getCpuUsed() + w.getMemoryUsed()));
    }
    @Override
    public String name() {
        return "greedy-first-fit";
    }
}
    

