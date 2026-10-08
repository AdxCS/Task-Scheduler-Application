package com.example.taskscheduler.scheduling;

import com.example.taskscheduler.domain.Job;
import com.example.taskscheduler.domain.Worker;
import com.example.taskscheduler.scheduling.JobAssignmentPlan;
import java.util.List;

public interface BatchSchedulingStratergy {
    List<JobAssignmentPlan> plan(List<Job> jobs, List<Worker> workers);
    String name();
}
