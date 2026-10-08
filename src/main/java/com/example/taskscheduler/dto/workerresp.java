package com.example.taskscheduler.dto;
import com.example.taskscheduler.domain.Worker;
import com.example.taskscheduler.domain.workerstatus;
public record workerresp(
    Long id,
    String Name,
    Integer cpuCapacity,
    Integer memoryCapacity,
    Integer cpuUsed,
    Integer memoryUsed,
    workerstatus status

){
    public static workerresp from(Worker worker){
        return new workerresp(
            worker.getId(),
            worker.getName(),
            worker.getCpuCapacity(),
            worker.getMemoryCapacity(),
            worker.getCpuUsed(),
            worker.getMemoryUsed(),
            worker.getStatus()
        );

    }
}
    

