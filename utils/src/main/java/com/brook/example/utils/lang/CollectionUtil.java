package com.brook.example.utils.lang;

import java.util.Iterator;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/7
 */
public interface CollectionUtil {

    /**
     * 以 conjunction 为分隔符将集合转换为字符串
     *
     * @param <T>         被处理的集合
     * @param iterable    {@link Iterable}
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    static <T> String join(Iterable<T> iterable, String conjunction) {
        if (null == iterable) {
            return null;
        }
        return join(iterable.iterator(), conjunction);
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串
     *
     * @param <T>         被处理的集合
     * @param iterator    集合
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    static <T> String join(Iterator<T> iterator, String conjunction) {
        if (null == iterator) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        T item;
        while (iterator.hasNext()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }

            item = iterator.next();
            if (ArrayUtil.isArray(item)) {
                sb.append(ArrayUtil.join(ArrayUtil.wrap(item), conjunction));
            } else if (item instanceof Iterable<?>) {
                sb.append(join((Iterable<?>) item, conjunction));
            } else if (item instanceof Iterator<?>) {
                sb.append(join((Iterator<?>) item, conjunction));
            } else {
                sb.append(item);
            }
        }
        return sb.toString();
    }
}
