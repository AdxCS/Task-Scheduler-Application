package com.example.taskscheduler.domain;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity 
@Table(name="Assignments")
@Getter @Setter 
@NoArgsConstructor 
public class Assignment {
    @Id @GeneratedValue (strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="job_id", nullable=false)
    private Job Job;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="worker_id", nullable=false)
    private Worker worker;
    @Column(nullable=false)
    private String stratergyUsed;
    @Column(nullable=false)
    private Instant assignedAt=Instant.now();
    public Assignment(Job job, Worker worker, String stratergyUsed) {
        this.Job = job;
        this.worker = worker;
        this.stratergyUsed = stratergyUsed;
    }

    
}
