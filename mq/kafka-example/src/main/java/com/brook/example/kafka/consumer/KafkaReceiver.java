package com.brook.example.kafka.consumer;

import com.brook.example.kafka.domain.Message;
import com.brook.example.utils.mapper.JsonMapper;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaReceiver {

  @KafkaListener(topics = {"brook"})
  public void listen(ConsumerRecord<?, ?> record) {
    Optional<?> kafkaMessage = Optional.ofNullable(record.value());

    if (kafkaMessage.isPresent()) {
      Message message = JsonMapper.INSTANCE.fromJson(kafkaMessage.get().toString(),Message.class);
      log.info(" record = {}", record);
      log.info(" message = {}", message);
    }

  }
}