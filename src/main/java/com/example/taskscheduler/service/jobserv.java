package com.example.taskscheduler.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.example.taskscheduler.domain.Job;
import com.example.taskscheduler.domain.jobstatus;
import com.example.taskscheduler.dto.jobcreaterequest;
import com.example.taskscheduler.messeger.jobeventpublisher;
import com.example.taskscheduler.messeger.jobsubmittedevent;
import com.example.taskscheduler.repository.jobrepo;
@Service 


public class jobserv {
    private final jobrepo jobrepo;
    private final jobeventpublisher jobeventpublisher;
    public jobserv(jobrepo jobrepo, jobeventpublisher jobeventpublisher){
        this.jobrepo = jobrepo;
        this.jobeventpublisher = jobeventpublisher;
    }
    @Transactional
    public Job createJob(jobcreaterequest reqs){
        Optional<Job> existing= jobrepo.findByIdempotencyKey(reqs.idempotencyKey());
        if (existing.isPresent()){
            return existing.get();
        }
        Job job=new Job();
        job.setIdempotencyKey(reqs.idempotencyKey());

        job.setType(reqs.type());
        job.setCpuRequired(reqs.cpuRequired());
        job.setMemoryRequired(reqs.memoryRequired());
        job.setPriority(reqs.priority());
        job.setDeadline(reqs.deadline());
        Job saved = jobrepo.save(job);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                jobeventpublisher.publishJobSubmittedEvent(new jobsubmittedevent(saved.getId()));
            }
        });

        return saved;
    }
    @Transactional(readOnly =true)
    public Job findById(Long id){
        return jobrepo.findById(id).orElseThrow(()->new RuntimeException("Job not found with id: "+id));
    }
    @Transactional(readOnly =true)
    public List<Job> findAll(jobstatus status){
        if (status != null) {
            return jobrepo.findByStatus(status);
        }
        return jobrepo.findAll();
        }
    }