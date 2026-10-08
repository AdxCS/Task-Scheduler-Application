package com.example.taskscheduler.service;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskscheduler.domain.Worker;
import com.example.taskscheduler.repository.workerrepo;
@Service

public class workerserv {
    
    private final workerrepo workerrepo;
    public workerserv(workerrepo workerrepo){
        this.workerrepo = workerrepo;

    }
    @Transactional(readOnly = true)
    public List<Worker> findAll(){
        return workerrepo.findAll();

    }

}
