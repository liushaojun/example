package com.brook.example.metricse;

import com.brook.example.metricse.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MetricsExampleApplication {

  @Autowired
  MetricsService metricsService;

  public static void main(String[] args) {
    SpringApplication.run(MetricsExampleApplication.class, args);
  }

  @GetMapping
  public String metrics() {
    metricsService.metrics();
    return "Hello Metrics!";
  }
}
