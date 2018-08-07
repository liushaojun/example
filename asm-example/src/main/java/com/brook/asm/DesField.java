package com.brook.asm;

import com.brook.asm.filter.DesFilter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DesField {

  /**
   * 加密类型
   */
  Class<? extends DesFilter> value() default DesFilter.class;

}