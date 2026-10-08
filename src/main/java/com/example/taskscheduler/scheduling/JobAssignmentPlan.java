package com.example.taskscheduler.scheduling;

import com.example.taskscheduler.domain.Job;

public record JobAssignmentPlan(Job job, int workerIndex) {
}
