package com.brook.example.netty.im.model;

import java.io.Serializable;
import lombok.Data;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/7
 */
@Data
public class Message implements Serializable{

  private static final long serialVersionUID = -1627810511211521424L;
  private String id;
  private String content;
}
