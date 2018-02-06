package com.brook.example.batch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Person {
    private Long id;
    private String lastName;
    private String firstName;
    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
}