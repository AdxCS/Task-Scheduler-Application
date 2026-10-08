package com.example.taskscheduler.service;

import com.example.taskscheduler.domain.jobstatus;
import com.example.taskscheduler.domain.Worker;
import com.example.taskscheduler.domain.workerstatus;
import com.example.taskscheduler.repository.jobrepo;
import com.example.taskscheduler.repository.workerrepo;
import com.example.taskscheduler.scheduling.BatchSchedulingStratergy;
import com.example.taskscheduler.scheduling.JobAssignmentPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchSchedulingService {

    private static final Logger log = LoggerFactory.getLogger(BatchSchedulingService.class);

    private final jobrepo jobRepository;
    private final workerrepo workerRepository;
    private final BatchSchedulingStratergy batchSchedulingStrategy;
    private final JobSchedulingService jobSchedulingService;

    public BatchSchedulingService(jobrepo jobRepository,
                                   workerrepo workerRepository,
                                   BatchSchedulingStratergy batchSchedulingStrategy,
                                   JobSchedulingService jobSchedulingService) {
        this.jobRepository = jobRepository;
        this.workerRepository = workerRepository;
        this.batchSchedulingStrategy = batchSchedulingStrategy;
        this.jobSchedulingService = jobSchedulingService;
    }

    @Scheduled(fixedDelayString = "${scheduler.genetic.batch-interval-ms:15000}")
    public void runBatch() {
        List<com.example.taskscheduler.domain.Job> pendingJobs = jobRepository.findByStatus(jobstatus.PENDING);
        if (pendingJobs.isEmpty()) return;

        List<Worker> candidates = workerRepository.findByStatus(workerstatus.ONLINE).stream()
                .filter(Worker::hasCapacity)
                .toList();
        if (candidates.isEmpty()) {
            log.debug("Batch run skipped: {} job(s) pending but no worker has capacity", pendingJobs.size());
            return;
        }

        List<JobAssignmentPlan> plan = batchSchedulingStrategy.plan(pendingJobs, candidates);
        log.info("Batch run via {}: placed {}/{} pending job(s)",
                batchSchedulingStrategy.name(), plan.size(), pendingJobs.size());

        for (JobAssignmentPlan decision : plan) {
            Long workerId = candidates.get(decision.workerIndex()).getId();
            jobSchedulingService.applyAssignment(decision.job().getId(), workerId, batchSchedulingStrategy.name());
        }
    }
}