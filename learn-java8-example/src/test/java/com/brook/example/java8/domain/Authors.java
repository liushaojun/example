package com.brook.example.java8.domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/31
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Authors {
    Author[] value() default {};
}