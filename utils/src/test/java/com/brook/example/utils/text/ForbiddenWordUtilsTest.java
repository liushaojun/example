package com.brook.example.utils.text;

import org.junit.Assert;
import org.junit.Test;

/**
 * ForbiddenWordUtils test case.
 *
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/28
 */
public class ForbiddenWordUtilsTest {
    @Test
    public void testReplaceWithDefaultMask() {

        String input = "你是傻逼";
        String expected = "你是***";
        String actual = ForbiddenWordUtils.replace(input);
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void testReplaceWithDefaultMask2() {

        String input = "你是主席吗";
        String expected = "你是***吗";
        String actual = ForbiddenWordUtils.replace(input);
        Assert.assertEquals(expected, actual);

    }


    @Test
    public void testReplaceWithDefaultMask3() {

        String input = "傻B123";
        String expected = "***123";
        String actual = ForbiddenWordUtils.replace(input);
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void testReplaceWithCustomMask() {
        String input = "傻B123";
        String expected = "###123";
        String actual = ForbiddenWordUtils.replace(input,"###");
        Assert.assertEquals(expected, actual);
    }

}