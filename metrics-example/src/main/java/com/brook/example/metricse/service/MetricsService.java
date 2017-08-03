package com.brook.example.metricse.service;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/3
 */
@Service
public class MetricsService {

  @Autowired
  Histogram metricsSize;
  @Autowired
  Counter pendingJobs;

  @Timed(name = "metrics-time")
  @Metered(name = "metrics-meter(tps)")
  public void metrics(){
    metricsSize.update(new Random().nextInt(10));
    this.counter();
  }

  public void counter(){
    pendingJobs.inc();
  }
}
