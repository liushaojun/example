package com.brook.example.security.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import org.junit.Test;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;

interface Car {

  String name();
}

/**
 * Spring 代理模式
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/30
 */
public class ProxyFactoryTest {

  @Test
  public void name() {
    ProxyFactory factory = new ProxyFactory(new BaoMa());
    factory.addInterface(Car.class);
    factory.addAdvice(new BeforeAdvice());
    factory.setExposeProxy(true);
    Car car = (Car) factory.getProxy();
    assertThat(car.name()).isEqualTo("宝马");
  }

}

class BaoMa implements Car {

  @Override
  public String name() {
    return "宝马";
  }
}

class BeforeAdvice implements MethodBeforeAdvice {

  @Override
  public void before(Method method, Object[] objects, Object o) throws Throwable {
    if (method.getName().equals("name")) {
      System.out.println("This's white baoma!");
    }
  }
}
