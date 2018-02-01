package com.brook.example.rabbit;

import com.brook.example.rabbit.client.RabbitMQClient;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/2/1
 */
@SpringBootApplication
@Slf4j
public class App implements CommandLineRunner{

  @Autowired
  RabbitMQClient client;

  @Bean
  public Queue queue(){
    return new Queue("brook");
  }
  public static void main(String[] args) {
    SpringApplication.run(App.class,args);
  }

  @Override
  public void run(String... strings) throws Exception {
    log.info("Boot init ......");
//    seq();
    parallel();
  }

  private void seq(){
    client.send("Send message brook");
  }

  private void parallel() {
    StopWatch watch = new StopWatch();
    watch.start();

    int threads = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(threads);

    final CountDownLatch start = new CountDownLatch(1);
    final CountDownLatch end = new CountDownLatch(threads);

    for (int i = 0; i < threads; i++) {
      executorService.execute(() -> {
        try {
          start.await();
          for (int i1 = 0; i1 < 10000; i1++) {
            client.send("Send message "+i1);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          end.countDown();
        }
      });
    }
    start.countDown();
    try {
      end.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      executorService.shutdown();
    }
    watch.stop();
    log.info("Send message use times:{} s", watch.getTotalTimeSeconds());

  }
}
