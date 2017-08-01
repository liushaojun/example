package com.brook.example.java8.functions;

import com.google.common.primitives.Ints;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class LazyLists {

    @Test
    void test() {
        MyList<Integer> l = new MyLinkedList<>(5, new MyLinkedList<>(10,
                new Empty<Integer>()));
        assertThat(l.head()).isEqualTo(5);
        LazyList<Integer> numbers = from(2);

        int two = numbers.head();
        assertThat(two).isEqualTo(2);
        int three = numbers.tail().head();
        assertThat(three).isEqualTo(3);
        int four = numbers.tail().tail().head();
        assertThat(four).isEqualTo(4);
        numbers = from(2);
        int prime_two = primes(numbers).head();
        int prime_three = primes(numbers).tail().head();
        int prime_five = primes(numbers).tail().tail().head();
        assertThat( Ints.asList(prime_two,prime_three,prime_five))
                .isEqualTo(Ints.asList(2,3,5));

        printAll(primes(from(2)),i -> i >=23);
    }

    interface MyList<T> {
        T head();

        MyList<T> tail();

        default boolean isEmpty() {
            return false;
        }

        MyList<T> filter(Predicate<T> p);
    }

    static class MyLinkedList<T> implements MyList<T> {
        final T head;
        final MyList<T> tail;

        public MyLinkedList(T head, MyList<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        public T head() {
            return head;
        }

        public MyList<T> tail() {
            return tail;
        }

        public boolean isEmpty() {
            return false;
        }

        public MyList<T> filter(Predicate<T> p) {
            return isEmpty() ? this : p.test(head()) ? new MyLinkedList<>(
                    head(), tail().filter(p)) : tail().filter(p);
        }
    }

    static class Empty<T> implements MyList<T> {
        public T head() {
            throw new UnsupportedOperationException();
        }

        public MyList<T> tail() {
            throw new UnsupportedOperationException();
        }

        public MyList<T> filter(Predicate<T> p) {
            return this;
        }
    }

    static class LazyList<T> implements MyList<T> {
        final T head;
        final Supplier<MyList<T>> tail;

        // tail 会延迟计算
        public LazyList(T head, Supplier<MyList<T>> tail) {
            this.head = head;
            this.tail = tail;
        }

        public T head() {
            return head;
        }

        public MyList<T> tail() {
            return tail.get();
        }

        public boolean isEmpty() {
            return false;
        }

        public MyList<T> filter(Predicate<T> p) {
            return isEmpty() ? this : p.test(head()) ? new LazyList<>(head(),
                    () -> tail().filter(p)) : tail().filter(p);
        }

    }

    public static LazyList<Integer> from(int n) {
        return new LazyList<>(n, () -> from(n + 1));
    }

    public static MyList<Integer> primes(MyList<Integer> numbers) {
        return new LazyList<>(numbers.head(), () -> primes(numbers.tail()
                .filter(n -> n % numbers.head() != 0)));
    }


    static <T> void printAll(MyList<T> numbers,Predicate<T> untail) {
        if (numbers.isEmpty()) {
            return;
        }
        if(untail.test(numbers.head())) return ;
        System.out.println(numbers.head());
        printAll(numbers.tail(),untail);
    }

}
