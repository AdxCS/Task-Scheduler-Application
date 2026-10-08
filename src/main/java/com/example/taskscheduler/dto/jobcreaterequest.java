package com.example.taskscheduler.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
public record jobcreaterequest (
    @NotBlank(message="Idempotency Key Is Required")
    String idempotencyKey,
    @NotBlank(message="type is required")
    String type,
    @NotBlank (message="name is required")
    String name,
    @Min(value=0, message="priority must be >=0")
    Integer priority,
    @Min(value=1, message="cpuRequired must be >=1")
    Integer cpuRequired,
    @Min(value=1, message="memoryRequired must be >=1")
    Integer memoryRequired,
    @Min (value=1,message="deadline is required")
    java.time.Instant deadline,
    @NotBlank(message="payload is required")

    String payload
) {
    public int priorityOrDefault(){
        return priority == null? 0 : priority;
    }
    

    }


