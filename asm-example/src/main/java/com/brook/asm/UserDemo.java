package com.brook.asm;

import com.brook.asm.filter.IdCardDesFilter;
import com.brook.asm.filter.MobileDesFilter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Brook 😈
 * @since 2018/7/23
 */
@Desensitized
@Getter
@Setter
public class UserDemo {

  @DesField(MobileDesFilter.class)
  private String name;
  @DesField(IdCardDesFilter.class)
  private String idCard;

  @Override
  public String toString() {
    return "UserDemo{" +
        "name='" + name + '\'' +
        ", idCard='" + idCard + '\'' +
        '}';
  }

  public static void main(String[] args) throws Exception {
    UserDemo demo = new UserDemo();
    demo.setName("18428368642");
    demo.setIdCard("22321321321");
    System.out.println(demo);
  }
}
