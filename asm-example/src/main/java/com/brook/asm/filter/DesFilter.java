package com.brook.asm.filter;

/**
 * @author Brook 😈
 * @since 2018/7/24
 */
public interface DesFilter<T> {

  default T desc(T value) {
    return value;
  }
}
