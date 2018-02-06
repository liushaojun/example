package com.brook.example.nats;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import nats.client.MessageHandler;
import nats.client.Nats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@Slf4j
@SpringBootApplication
public class NatsApp implements CommandLineRunner{
    @Autowired
    Nats nats;
    public static void main( String[] args ) {
        SpringApplication.run(NatsApp.class,args);
    }

    @Override
    public void run(String... strings) {
        subscribe();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            // ignore.
        }
        send();
    }


    // 订阅消息
    public void subscribe() {
        nats.subscribe("brook",
            (MessageHandler) message -> log.info("Received: {}", message.getBody()));
    }

    // 发布消息
    public void send() {
        log.info("Begin send message:");
        nats.publish("brook", "Hello nats.");
    }
}
