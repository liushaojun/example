package com.brook.asm;


import com.brook.asm.filter.DesFilter;
import lombok.Data;

@Data
public class FiledInfo {

  private int acc;
  private String name;
  private String type;
  private String generic;
  private Object defaultValue;
  private boolean enabled;
  private Class<? extends DesFilter> filterClass;

  public FiledInfo(int acc, String name, String type, String generic, Object defaultValue) {
    this.acc = acc;
    this.name = name;
    this.type = type;
    this.generic = generic;
    this.defaultValue = defaultValue;
  }
}
