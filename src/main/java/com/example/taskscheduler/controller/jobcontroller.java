package com.example.taskscheduler.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.taskscheduler.domain.Job;
import com.example.taskscheduler.domain.jobstatus;
import com.example.taskscheduler.dto.jobcreaterequest;
import com.example.taskscheduler.dto.jobresp;
import com.example.taskscheduler.service.jobserv;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/jobs")


public class jobcontroller {
    private final jobserv jobservice;
    public jobcontroller(jobserv jobservice){
        this.jobservice= jobservice;
    }
    @PostMapping
    public ResponseEntity<jobresp> submit(@Valid @RequestBody jobcreaterequest request, UriComponentsBuilder uriBuilder){
        Job job = submitJob(request);
        var location = uriBuilder.path("/jobs/{id}").buildAndExpand(job.getId()).toUri();
        return ResponseEntity.accepted().location(location).body(jobresp.from(job));

    }

    private Job submitJob(jobcreaterequest request) {
        for (String methodName : List.of("submit", "create", "save")) {
            try {
                var method = jobservice.getClass().getMethod(methodName, jobcreaterequest.class);
                return (Job) method.invoke(jobservice, request);
            } catch (NoSuchMethodException ignored) {
                // Try the next likely service method name.
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Unable to submit job", e);
            }
        }

        throw new IllegalStateException("No compatible submit method found on job service");
    }
    @GetMapping("/{id}")
    public ResponseEntity<jobresp> getJobById(@PathVariable Long id){
        Job job=jobservice.findById(id);
        return ResponseEntity.ok(jobresp.from(job));
    }
    @GetMapping
    public ResponseEntity<List<jobresp>> getAllJobs(@RequestParam(required = false) jobstatus status){
        List<jobresp> jobs=jobservice.findAll(status).stream().map(jobresp::from)
            .toList();
        return ResponseEntity.ok(jobs);
    }
    
    
}
