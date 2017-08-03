package com.brook.example.metricse.config;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/3
 */
@EnableMetrics
@Configuration
public class MetricsConfig extends MetricsConfigurerAdapter{

  @Bean
  Counter pendingJobs(MetricRegistry registry){
    return registry.counter("metrics-counter");
  }


  @Bean
  public Histogram responseSizes(MetricRegistry metrics) {
    return metrics.histogram("metrics-sizes");
  }
  @Override
  public void configureReporters(MetricRegistry metricRegistry) {
    registerReporter(ConsoleReporter
        .forRegistry(metricRegistry)
        .build())
        .start(1, TimeUnit.MINUTES);
  }
}
