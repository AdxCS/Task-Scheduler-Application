package com.example.taskscheduler.domain;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "workers")
@Getter
@Setter 
@NoArgsConstructor
public class Worker{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private Integer cpuCapacity;
    @Column(nullable=false)
    private Integer memoryCapacity;
    @Column(nullable=false)
    private Integer cpuused=0;
    @Column(nullable=false)
    private Integer currentload=0;
    @Column(nullable=false)
    private Integer memoryused=0;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private workerstatus status=workerstatus.IDLE;
    @OneToMany (mappedBy="worker", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Job> jobs = new ArrayList<>();
    @Version 
    private long version;
    public Worker(String name, Integer cpuCapacity, Integer memoryCapacity,Integer cpuused,Integer memoryused,workerstatus status){ {
        this.name = name;
        this.cpuCapacity = cpuCapacity;
        this.memoryCapacity = memoryCapacity;
        this.cpuused=cpuused;
        this.memoryused=memoryused;
        this.status=status;}

    }
    // Inside Worker.java
    public int getCpuUsed() {
    return this.cpuused;
}
    
    public int getMemoryUsed() {
        return this.memoryused;
    }
    public int getCurrentLoad() {
        return this.currentload;
    }
    public int getCapacity() {
        return this.cpuCapacity + this.memoryCapacity;
    }
    public void setCpuUsed(int cpuUsed) {
        this.cpuused = cpuUsed;
    }

    public void setMemoryUsed(int memoryUsed) {
        this.memoryused = memoryUsed;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentload = currentLoad;
    }
   public boolean hascapacityfor(int cpu,int memory){
        return (this.cpuused + cpu <= this.cpuCapacity) && (this.memoryused + memory <= this.memoryCapacity);
    }
}