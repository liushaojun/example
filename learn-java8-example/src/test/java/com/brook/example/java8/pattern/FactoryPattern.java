package com.brook.example.java8.pattern;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;


public class FactoryPattern {

    final static private Map<String, Supplier<Product>> map = new HashMap<>();

    static {
        map.put("cup", Cup::new);
        map.put("vacuumCup", VacuumCup::new);
        map.put("wine", Wine::new);
    }

    @Test
    void test() {
        Product p1 = ProductFactory.createProduct("cup");
        assertThat(p1).isInstanceOf(Cup.class);
        Supplier<Product> wineSupplier = Wine::new;
        Product p2 = wineSupplier.get();
        assertThat(p2).isInstanceOf(Wine.class);
        Product p3 = ProductFactory.createProductLambda("vacuumCup");
        assertThat(p3).isInstanceOf(VacuumCup.class);

    }

    private interface Product {
    }

    static private class ProductFactory {
        public static Product createProduct(String name) {
            switch (name) {
                case "cup":
                    return new Cup();
                case "vacuumCup":
                    return new VacuumCup();
                case "wine":
                    return new Wine();
                default:
                    throw new RuntimeException("No such product " + name);
            }
        }

        public static Product createProductLambda(String name) {
            Supplier<Product> p = map.get(name);
            if (p != null) return p.get();
            throw new RuntimeException("No such product " + name);
        }
    }

    static private class Cup implements Product {
    }

    static private class VacuumCup implements Product {
    }

    static private class Wine implements Product {
    }
}
