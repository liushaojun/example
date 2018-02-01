package com.brook.example.rabbit.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQServer {

    @RabbitListener(queues = "brook")
    public void receive(String message) {
        log.info("You received message is：{}" , message);
    }
}