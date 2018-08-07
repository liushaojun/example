package com.brook.asm.filter;

import com.brook.asm.utils.DesensitizeUtil;

/**
 * @author Brook 😈
 * @since 2018/7/24
 */
public class MobileDesFilter implements DesFilter<String> {

  @Override
  public String desc(String value) {
    return DesensitizeUtil.mobile(value);
  }
}
