package com.brook.example.security.service;

import com.brook.example.security.consts.G;
import com.brook.example.security.domain.UserEntity;
import com.brook.example.security.domain.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户业务类
 *
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/17
 */
@Log4j2
@Service
public class UserService {

  final UserRepository userRepository;

  @Autowired
  UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * 添加用户
   * @param userEntity 用户实体
   */
  public void insert(UserEntity userEntity) {
    String username = userEntity.getUsername();
    if (exist(username))
      throw new IllegalArgumentException("该用户名已被占用！");
    encryptPassword(userEntity);
    userEntity.setRoles(G.ROLE_USER);
    userRepository.save(userEntity);
  }

  public UserEntity findBy(String username) {
    return userRepository.findByUsername(username);
  }

  /**
   * 判断用户是否存在
   *
   * @param username 账号
   * @return 密码
   */
  public boolean exist(String username) {
    UserEntity userEntity = this.findBy(username);
    return (userEntity != null);
  }

  /**
   * 加密密码
   */
  public void encryptPassword(UserEntity userEntity) {
    String password = userEntity.getPassword();
    password = new BCryptPasswordEncoder().encode(password);
    userEntity.setPassword(password);
  }

  public static void main(String[] args) {
    System.out.println("output:>>>" + new BCryptPasswordEncoder().encode
        ("123456"))
    ;
  }
}
