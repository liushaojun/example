package com.brook.example.java8.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/30
 */
@Data
@NoArgsConstructor
public class Car {
    private String name;
    public Car(String name){
        this.name = name;
    }
    private Optional<Insurance> insurance;
    @Data
    @NoArgsConstructor
    public static class Insurance {
        private String name;
        public Insurance(String name){
            this.name = name;
        }
    }
}
