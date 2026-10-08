package com.example.taskscheduler.scheduling;
import com.example.taskscheduler.domain.Job;
import com.example.taskscheduler.domain.*;
import com.example.taskscheduler.domain.Worker;
import org.springframework.stereotype.Component;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
@Component
public class GreedyScheduler implements Schedulingstratergy{
    @Override
    public String name() {
        return "greedy";
    }

    @Override
    public Optional<Worker> selectWorker(Job job, List<Worker> workers) {
        return workers.stream()
                .filter(worker -> worker.hascapacityfor(job.getCpuRequired(), job.getMemoryRequired()))
                .min(Comparator.comparingInt(Worker::getCpuUsed));
    }
}
