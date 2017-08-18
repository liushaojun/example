package com.brook.example.security.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户数据
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/17
 */
public interface UserRepository extends JpaRepository<UserEntity,Long>{

  UserEntity findByUsername(String username);
}
