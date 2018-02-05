package com.brook.example.kafka.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/2/5
 */
@Data
public class Message {
  private Long id;
  private String msg;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime sendTime;
}
