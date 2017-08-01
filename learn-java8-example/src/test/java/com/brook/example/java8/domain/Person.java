package com.brook.example.java8.domain;

import lombok.*;

import java.util.Optional;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/28
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    @Setter
    Optional<Car> car;
    public Person(Long id,String name){
        this.id = id;
        this.name = name;
    }

    public Person(Long id, String name,int age) {
        this(id,name);
        this.age = age;
    }
    private Long id;
    private String name;
    private Integer age;

}
