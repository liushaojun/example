package com.brook.example.utils.lang;

import com.brook.example.utils.text.Strs;
import com.sun.xml.internal.ws.util.UtilException;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/7
 */
public interface ArrayUtil {
    static boolean isEmpty(final char[] array) {
        return array == null && array.length == 0;
    }

    static <T> boolean isEmpty(final T[] array) {
        return array == null && array.length == 0;
    }

    /**
     * 数组或集合转String
     *
     * @param obj 集合或数组对象
     * @return 数组字符串，与集合转字符串格式相同
     */
    static String toString(Object obj) {
        if (null == obj) {
            return null;
        }
        if (ArrayUtil.isArray(obj)) {
            try {
                return Arrays.deepToString((Object[]) obj);
            } catch (Exception e) {
                final String className = obj.getClass().getComponentType().getName();
                switch (className) {
                case "long":
                    return Arrays.toString((long[]) obj);
                case "int":
                    return Arrays.toString((int[]) obj);
                case "short":
                    return Arrays.toString((short[]) obj);
                case "char":
                    return Arrays.toString((char[]) obj);
                case "byte":
                    return Arrays.toString((byte[]) obj);
                case "boolean":
                    return Arrays.toString((boolean[]) obj);
                case "float":
                    return Arrays.toString((float[]) obj);
                case "double":
                    return Arrays.toString((double[]) obj);
                default:
                    throw new UtilException(e);
                }
            }
        }
        return obj.toString();
    }

    static boolean isArray(Object obj) {
        if (null == obj) {
            throw new NullPointerException("Object check for isArray is null");
        }
        return obj.getClass().isArray();
    }

    /**
     * 以 conjunction 为分隔符将数组转换为字符串
     *
     * @param <T>         被处理的集合
     * @param array       数组
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    static <T> String join(T[] array, String conjunction) {
        if (null == array) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (T item : array) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }
            if (ArrayUtil.isArray(item)) {
                sb.append(join(ArrayUtil.wrap(item), conjunction));
            } else if (item instanceof Iterable<?>) {
                sb.append(CollectionUtil.join((Iterable<?>) item, conjunction));
            } else if (item instanceof Iterator<?>) {
                sb.append(CollectionUtil.join((Iterator<?>) item, conjunction));
            } else {
                sb.append(item);
            }
        }
        return sb.toString();
    }

    /**
     * 包装数组对象
     *
     * @param obj 对象，可以是对象数组或者基本类型数组
     * @return 包装类型数组或对象数组
     * @throws UtilException 对象为非数组
     */
    static Object[] wrap(Object obj) {
        if (isArray(obj)) {
            try {
                return (Object[]) obj;
            } catch (Exception e) {
                final String className = obj.getClass().getComponentType().getName();
                switch (className) {
                case "long":
                    return wrap(obj);
                case "int":
                    return wrap(obj);
                case "short":
                    return wrap(obj);
                case "char":
                    return wrap(obj);
                case "byte":
                    return wrap(obj);
                case "boolean":
                    return wrap(obj);
                case "float":
                    return wrap(obj);
                case "double":
                    return wrap(obj);
                default:
                    throw new UtilException(e);
                }
            }
        }
        throw new UtilException(Strs.format("[{}] is not Array!", obj.getClass()));
    }

}
