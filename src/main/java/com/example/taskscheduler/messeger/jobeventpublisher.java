package com.example.taskscheduler.messeger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.taskscheduler.config.RabbitMQConfig;
@Component 
public class jobeventpublisher {
    private final RabbitTemplate rabbitTemplate;
    public jobeventpublisher(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate=rabbitTemplate;
    }
    public void publishJobSubmittedEvent(jobsubmittedevent event){
        rabbitTemplate.convertAndSend(RabbitMQConfig.JOB_EXCHANGE,RabbitMQConfig.JOB_SUBMITTED_ROUTING_KEY,event);
    }   
    
}
