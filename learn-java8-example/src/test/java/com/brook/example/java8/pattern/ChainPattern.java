package com.brook.example.java8.pattern;

import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ChainPattern {

    @Test
    void test(){
        // 传统的设计模式
        ProcessingObject<String> p1 = new HeaderTextProcessing();
        ProcessingObject<String> p2 = new SpellCheckerProcessing();
        p1.setSuccessor(p2);
        String result1 = p1.handle("Aren't labdas really sexy?!!");
        System.out.println(result1);
        // 使用java8 函数编程重构
        UnaryOperator<String> headerProcessing =
                (String text) -> "From Raoul, Mario and Alan: " + text;
        UnaryOperator<String> spellCheckerProcessing =
                (String text) -> text.replaceAll("labda", "lambda");
        Function<String, String> pipeline = headerProcessing.andThen(spellCheckerProcessing);
        String result2 = pipeline.apply("Aren't labdas really sexy?!!");
        assertEquals(result1,result2);
    }

    static private abstract class ProcessingObject<T> {
        protected ProcessingObject<T> successor;

        public void setSuccessor(ProcessingObject<T> successor) {
            this.successor = successor;
        }

        public T handle(T input) {
            T r = handleWork(input);
            if (successor != null) {
                return successor.handle(r);
            }
            return r;
        }

        abstract protected T handleWork(T input);
    }

    static private class HeaderTextProcessing
            extends ProcessingObject<String> {
        public String handleWork(String text) {
            return "From Raoul, Mario and Alan: " + text;
        }
    }

    static private class SpellCheckerProcessing
            extends ProcessingObject<String> {
        public String handleWork(String text) {
            return text.replaceAll("labda", "lambda");
        }
    }
}


