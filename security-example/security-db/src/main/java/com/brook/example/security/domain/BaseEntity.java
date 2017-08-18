package com.brook.example.security.domain;

import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * entity 基础类
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/17
 */
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
public abstract class BaseEntity<PK> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected PK id;
  protected LocalDateTime createAt;
  protected LocalDateTime updateAt;

}
