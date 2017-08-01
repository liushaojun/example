package com.brook.example.java8.domain;

import java.lang.annotation.*;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/31
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Authors.class)
public @interface Author {
    String name() default "";
}
