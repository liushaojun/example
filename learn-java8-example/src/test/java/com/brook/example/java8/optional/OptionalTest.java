package com.brook.example.java8.optional;

import com.brook.example.java8.domain.Car;
import com.brook.example.java8.domain.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @see java.util.Optional
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/29
 */
class OptionalTest {

    @DisplayName("Optional testcase.")
    @Test
    void create() {
        String isNull = null;
        isNull = Optional.ofNullable(isNull)
                .orElse("is null");
        List<String> strs = null;
        strs = Optional.ofNullable(strs)
                .orElseGet(Collections::emptyList);
        assertNotNull(strs);
        assertEquals(isNull, "is null");
        assertThrows(NullPointerException.class, () -> Optional.of(null));
        Assertions.assertThat(Optional.ofNullable("")).isPresent();
        assertThrows(IllegalArgumentException.class,
                () -> Optional.ofNullable(null).orElseThrow(IllegalArgumentException::new));
    }

    @Test
    void testGetName() {
        Person tom = new Person(1L, "tom");
        tom.setCar(Optional.empty());
        assertEquals("unknown",getInsuranceName(tom));
        Car.Insurance insurance = new Car.Insurance("阳光车险");
        Car car = new Car("宝马");
        car.setInsurance(Optional.of(insurance));
        tom.setCar(Optional.of(car));
        assertEquals("阳光车险",getInsuranceName(tom));
    }

    private String getInsuranceName(Person person) {
        return Optional.of(person)
                .flatMap(Person::getCar)
                .flatMap(Car::getInsurance)
                .map(Car.Insurance::getName)
                .orElse("unknown");
    }
}
