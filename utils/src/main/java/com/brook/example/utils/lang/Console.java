package com.brook.example.utils.lang;

import com.brook.example.utils.text.Strs;

import java.text.MessageFormat;
import java.util.function.Supplier;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * Console 控制台打印日志工具类
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/7
 */
public interface Console {

    /**
     * {@link System#out#println()}
     */
    static void log(){
        out.println();
    }

    /**
     * @param obj 要打印的对象
     */
     static void log(Object obj) {
        if (obj instanceof Throwable) {
            Throwable e = (Throwable) obj;
            log(e, e.getMessage());
        } else {
            log("{}", obj);
        }
    }

    /**
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   值
     */
    static void log(String template, Object... values) {
        log(null, template, values);
    }

    static <T> void log(Supplier<T> other){
       log(other.get());
    }
    /**
     * @param t        异常对象
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   值
     */
    static void log(Throwable t, String template, Object... values) {
        out.println(Strs.format(template, values));
        if (null != t) {
            t.printStackTrace();
            out.flush();
        }
    }

    //--------------------------------------------------------------------------------- Error

    /**
     * 同System.err.println()
     */
    static void error() {
        err.println();
    }

    /**
     * @param obj 打印的对象
     */
    static void error(Object obj) {
        if (obj instanceof Throwable) {
            Throwable e = (Throwable) obj;
            error(e, e.getMessage());
        } else {
            error("{}", obj);
        }
    }

    /**
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   值
     */
    static void error(String template, Object... values) {
        error(null, template, values);
    }

    /**
     *
     * @param t        异常对象
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   值
     */
    static void error(Throwable t, String template, Object... values) {
        err.println(MessageFormat.format(template, values));
        if (null != t) {
            t.printStackTrace(err);
            err.flush();
        }
    }

}
