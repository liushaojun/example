package com.brook.example.rabbit.client;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String message) {

        rabbitTemplate.convertAndSend("brook", message);
    }
}