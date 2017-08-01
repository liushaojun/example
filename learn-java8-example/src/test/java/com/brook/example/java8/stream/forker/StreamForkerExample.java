package com.brook.example.java8.stream.forker;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamForkerExample {

    @Test
    void testProcessMenu(){
        processMenu();
    }
    private static void processMenu() {
        Stream<Dish> menuStream = Dish.MENU.stream();

        StreamForker.Results results = new StreamForker<Dish>(menuStream)
                // 获取菜单
                .fork("shortMenu", s -> s.map(Dish::getName).collect(joining(", ")))
                // 统计卡路里
                .fork("totalCalories", s -> s.mapToInt(Dish::getCalories).sum())
                // 哪个菜单的热量最多
                .fork("mostCaloricDish", s -> s.reduce((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)
                        .get())
                // 按类型分组
                .fork("dishesByType", s -> s.collect(groupingBy(Dish::getType)))
                .getResults();

        String shortMenu = results.get("shortMenu");
        int totalCalories = results.get("totalCalories");
        Dish mostCaloricDish = results.get("mostCaloricDish");
        Map<Dish.Type, List<Dish>> dishesByType = results.get("dishesByType");

        assertEquals(Dish.MENU.size(),shortMenu.split(",").length);
        assertEquals(4300, totalCalories);
        assertEquals("pork",mostCaloricDish.getName());
        assertEquals(Dish.Type.values().length,dishesByType.size());
    }
}
