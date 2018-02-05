package com.brook.example.kafka;

import com.brook.example.kafka.provider.KafkaSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/2/5
 */
@SpringBootApplication
public class KafkaApp {

  @Bean
  public ObjectMapper objectMapper(){
    return new ObjectMapper();
  }
  @Autowired
  KafkaSender kafkaSender;
  public static void main(String... args) {
    ConfigurableApplicationContext context = SpringApplication.run(KafkaApp.class, args);
    KafkaSender sender = context.getBean(KafkaSender.class);
    for (int i = 0; i < 3; i++) {
      sender.send();
      try {
        TimeUnit.SECONDS.sleep(3);
      } catch (InterruptedException e) {
        // ignore.
      }
    }
  }
}
