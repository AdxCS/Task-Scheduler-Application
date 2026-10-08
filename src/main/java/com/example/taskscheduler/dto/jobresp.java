package com.example.taskscheduler.dto;
import java.time.Instant;

import com.example.taskscheduler.domain.Job;
import com.example.taskscheduler.domain.jobstatus;
public record jobresp(
    Long id,
    String name,
    Integer cpuRequired,
    Integer memoryRequired,
    Integer priority,
    Instant deadline,
    Instant submittedAt,
    jobstatus status,
    Long workerId

) {
    public static jobresp from(Job job) {
        return new jobresp(
                job.getId(),
                job.getAssignedWorker() != null ? job.getAssignedWorker().getName() : null,
                job.getCpuRequired(),
                job.getMemoryRequired(),
                job.getPriority(),
                job.getDeadline(),
                job.getCreatedAt(),
                job.getStatus(),
                job.getStatus() == jobstatus.RUNNING && job.getAssignedWorker() != null ? job.getAssignedWorker().getId() : null
 
        );
    }
}
