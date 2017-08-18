package com.brook.example.security.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ${DESCRIPTION}
 *
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;
  @Test
  public void findByUsername() throws Exception {
    UserEntity entity = userRepository.findByUsername("admin");
    assertThat(entity).isNotNull();
  }

}