package com.example.taskscheduler.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskscheduler.dto.workerresp;
import com.example.taskscheduler.service.workerserv;

@RestController @RequestMapping ("/workers")
public class workercontroller{
    private final workerserv workerservice;
    public workercontroller(workerserv workerservice){
        this.workerservice=workerservice;
    }
    @GetMapping
    public ResponseEntity<List<workerresp>> getAll(){
        List<workerresp> workers =workerservice.findAll().stream().map(workerresp::from).toList();
        return ResponseEntity.ok(workers);
    }
    
}
