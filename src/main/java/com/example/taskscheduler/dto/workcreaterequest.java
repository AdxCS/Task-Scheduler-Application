package com.example.taskscheduler.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
public record workcreaterequest (
    @NotBlank(message="name must not be empty")
    String name,
    @NotBlank (message="Cpucapacity must be mentioned")
    @Min (value=1, message="cpuCapacity must be >=1")
    Integer cpuCapacity,
    @NotBlank (message ="memoryCapacity need not be empty")
    @Min (value=1, message="memoryCapacity must be >=1")
    Integer memoryCapacity  
){}
    

