package com.brook.example.kafka.provider;

import com.brook.example.kafka.domain.Message;
import com.brook.example.utils.mapper.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/2/5
 */
@Service
@Slf4j
public class KafkaSender {

  @Autowired
  ObjectMapper mapper;
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  public void send() {
    Message message = new Message();
    message.setId(System.currentTimeMillis());
    message.setMsg(UUID.randomUUID().toString());
    message.setSendTime(LocalDateTime.now());
    String data = JsonMapper.INSTANCE.toJson(message);
    log.info("+++++++++++++++++++++  message = {}",data );
    kafkaTemplate.send("brook", data);
  }
}
