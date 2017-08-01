package com.brook.example.java8.pattern;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StrategyPattern {

    public final static String  NUMBERIC ="\\d+";
    public final static String LOWER_CASE ="[a-z]+";
    @Test
    void test() {
        // old school
        Validator v1 = new Validator(new IsNumeric());
        assertFalse(v1.validate("abc"));
        Validator v2 = new Validator(new IsAllLowerCase());
        assertTrue(v2.validate("abc"));

        // with lambdas
        Validator v3 = new Validator((String s) -> s.matches(NUMBERIC));
        assertFalse(v3.validate("abc"));
        Validator v4 = new Validator((String s) -> s.matches(LOWER_CASE));
        assertTrue(v4.validate("abc"));
    }

    interface ValidationStrategy {
        boolean execute(String s);
    }

    static private class IsAllLowerCase implements ValidationStrategy {
        public boolean execute(String s) {
            return s.matches(LOWER_CASE);
        }
    }

    static private class IsNumeric implements ValidationStrategy {
        public boolean execute(String s) {
            return s.matches(NUMBERIC);
        }
    }

    static private class Validator {
        private final ValidationStrategy strategy;

        public Validator(ValidationStrategy v) {
            this.strategy = v;
        }

        public boolean validate(String s) {
            return strategy.execute(s);
        }
    }
}
