package com.example.taskscheduler.scheduling;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.taskscheduler.domain.Assignment;
import com.example.taskscheduler.domain.Job;
import com.example.taskscheduler.domain.Worker;
import com.example.taskscheduler.domain.jobstatus;
import com.example.taskscheduler.domain.workerstatus;
import com.example.taskscheduler.repository.assignmentrepo;
import com.example.taskscheduler.repository.jobrepo;
import com.example.taskscheduler.repository.workerrepo;

import jakarta.transaction.Transactional;

@Service
public class Jobschedulingservice {
    private static final Logger log = LoggerFactory.getLogger(Jobschedulingservice.class);
    private final jobrepo Jobrepo;
    private final workerrepo Workerrepo;
    private final assignmentrepo Assignmentrepo;
    private final Schedulingstratergy schedulingstratergy;
    private final jobstatus Jobstatus;

    public Jobschedulingservice(jobrepo Jobrepo,
                                workerrepo Workerrepo,
                                assignmentrepo Assignmentrepo,
                                Schedulingstratergy schedulingstratergy,
                                jobstatus Jobstatus) {
        this.Jobrepo = Jobrepo;
        this.Workerrepo = Workerrepo;
        this.Assignmentrepo = Assignmentrepo;
        this.schedulingstratergy = schedulingstratergy;
        this.Jobstatus = Jobstatus;
    }

    @Transactional
    public void scheduleJobs(Long jobId) {
        Job job = Jobrepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        if (job.getStatus() != jobstatus.PENDING) {
        log.info("Job {} is already {} -- skipping scheduling", jobId, job.getStatus());
        return;
    }
    var candidates = Workerrepo.findByStatus(workerstatus.ONLINE);
    var chosen = schedulingstratergy.selectWorker(job, candidates)
            .orElseThrow(() -> new IllegalStateException(
                    "No worker with capacity available for job " + jobId));

        applyAssignment(jobId, chosen.getId(), schedulingstratergy.name());
    }
    @Transactional
    public void applyAssignment(Long jobId, Long workerId, String strategyName) {
        Job job = Jobrepo.findById(jobId)
                .orElseThrow(() -> new IllegalStateException("Job " + jobId + " not found"));
 
        if (job.getStatus() != Jobstatus.PENDING) {
            log.info("Job {} is already {} -- skipping assignment from {}", jobId, job.getStatus(), strategyName);
            return;
        }
 
        Worker worker = Workerrepo.findById(workerId)
                .orElseThrow(() -> new IllegalStateException("Worker " + workerId + " not found"));
 
        if (!worker.hascapacityfor(job.getCpuRequired(), job.getMemoryRequired())) {
            log.warn("Worker {} no longer has capacity -- skipping assignment of job {} from {}",
                    workerId, jobId, strategyName);
            return;
        }
 
        job.setAssignedWorker(worker);
        job.setStatus(Jobstatus.ASSIGNED);
        worker.setStatus(workerstatus.values()[Math.min(worker.getStatus().ordinal() + 1,
                workerstatus.values().length - 1)]);

        Jobrepo.save(job);
        Workerrepo.save(worker);
 
        Assignment assignment = new Assignment();
        assignment.setJob(job);
        assignment.setWorker(worker);
        assignment.setStratergyUsed(strategyName);
        Assignmentrepo.save(assignment);
 
        log.info("Assigned job {} to worker {} via {}", jobId, workerId, strategyName);
    }
}
