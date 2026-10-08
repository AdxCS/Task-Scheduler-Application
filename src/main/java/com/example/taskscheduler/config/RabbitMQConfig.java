package com.example.taskscheduler.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMQConfig {
    public static final String JOB_EXCHANGE = "job.exchange";
    public static final String JOB_SUBMITTED_QUEUE = "job.submitted.queue";
    public static final String JOB_SUBMITTED_ROUTING_KEY = "job.submitted";
    public static final String JOB_SUBMITTED_DLQ = "job.submitted.dlq";
 
    @Bean
    public TopicExchange jobExchange() {
        return new TopicExchange(JOB_EXCHANGE);
    }
 
    @Bean
    public Queue jobSubmittedQueue() {
        return org.springframework.amqp.core.QueueBuilder.durable(JOB_SUBMITTED_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", JOB_SUBMITTED_DLQ)
                .build();
    }
 
    @Bean
    public Queue jobSubmittedDlq() {
        return org.springframework.amqp.core.QueueBuilder.durable(JOB_SUBMITTED_DLQ).build();
    }
 
    @Bean
    public Binding jobSubmittedBinding(Queue jobSubmittedQueue, TopicExchange jobExchange) {
        return BindingBuilder.bind(jobSubmittedQueue).to(jobExchange).with(JOB_SUBMITTED_ROUTING_KEY);
    }
 
    // Serializes/deserializes message bodies as JSON instead of Java's
    // default (and insecure) binary serialization.
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
 
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}


