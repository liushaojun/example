package com.brook.example.java8.base;

import com.brook.example.java8.domain.Author;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/31
 */
public class RepeateAnnoTest {
    @Test
    void getAuthors(){
        Author [] authors = Book.class.getAnnotationsByType(Author.class);
        List<String> names = Arrays.asList(authors)
                .stream()
                .map(Author::name)
                .collect(toList());
        assertIterableEquals(Lists.newArrayList("tom", "lucy", "小明"),names);
    }
}
@Author(name = "tom")
@Author(name = "lucy")
@Author(name = "小明")
class Book{

}