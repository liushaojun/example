package com.brook.example.java8.junit.extra.tags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/2
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("slow")
public @interface Slow {
}