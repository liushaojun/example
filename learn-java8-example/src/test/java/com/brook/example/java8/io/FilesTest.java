package com.brook.example.java8.io;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Files test case.
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/28
 */
@Log4j2
class FilesTest {
    static String home = System.getProperty("user.home");

    @Test
    void list() throws IOException {
        Files.list(Paths.get(home, "/Desktop"))
                .map(path -> path.getFileName() + " -->" + (path.toFile().isDirectory()
                        ? "目录" : "文件"))
                .forEach(System.out::println);

    }

    @Test
    void walk() throws IOException {
        String currentDir = getClass().getResource(".").getPath();
        String filesTest = Files.walk(Paths.get(currentDir), 2)
                .map(Path::getFileName)
                .map(Object::toString)
                .filter(fileName ->fileName.startsWith("FilesTest"))
                .findFirst()
                .orElse("");
        assertEquals("FilesTest.class",filesTest);

    }


}
