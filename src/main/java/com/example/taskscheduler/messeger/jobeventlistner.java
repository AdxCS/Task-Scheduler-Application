package com.example.taskscheduler.messeger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.taskscheduler.config.RabbitMQConfig;
import com.example.taskscheduler.domain.Job;
import com.example.taskscheduler.repository.jobrepo;
@Component
public class jobeventlistner {
 
    private static final Logger log = LoggerFactory.getLogger(jobeventlistner.class);
 
    private final jobrepo jobRepository;
 
    public jobeventlistner(jobrepo jobRepository) {
        this.jobRepository = jobRepository;
    }
 
    @RabbitListener(queues = RabbitMQConfig.JOB_SUBMITTED_QUEUE)
    public void onJobSubmitted(jobsubmittedevent event) {
        Job job = jobRepository.findById(event.jobId())
                .orElseThrow(() -> new IllegalStateException(
                        "Received job.submitted event for missing job id " + event.jobId()));
 
        log.info("Received job.submitted for job {} (type={}, priority={})",
                job.getId(), job.getType(), job.getPriority());
    }
}
