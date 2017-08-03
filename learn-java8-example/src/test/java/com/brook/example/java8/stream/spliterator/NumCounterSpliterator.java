package com.brook.example.java8.stream.spliterator;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Spliterator，类似于迭代器，用于遍历源的元素。 Spliterator API被设计为支持顺序遍历，
 * 通过支持分解以及单元素迭代来支持有效的并行遍历。 此外，通过Spliterator访问元素的协议
 * 被设计为比Iterator施加更小的单元素开销，并避免为hasNext()和next()分别使用单独的方法所涉及的内在竞争。
 * Spliterator 详细参考 http://blog.csdn.net/anonymousprogrammer/article/details/76034365
 */
class NumCounterSpliterator implements Spliterator<Character> {

    private String str;
    private int currentChar = 0;

    public NumCounterSpliterator(String str) {
        this.str = str;
    }

    /** 顺序处理每个元素
     * @param action
     * @return
     */
    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(str.charAt(currentChar++));
        return currentChar < str.length();
    }

    /**
     *  这就是为Spliterator专门设计的方法，区分与普通的Iterator，该方法会把当前元素
     *  划分一部分出去创建一个新的Spliterator作为返回，两个Spliterator变会并行执行，
     *  如果元素个数小到无法划分则返回null
     * @return
     */
    @Override
    public Spliterator<Character> trySplit() {

        int currentSize = str.length() - currentChar;
        if (currentSize < 10) return null;

        for (int pos = currentSize/2 + currentSize; pos < str.length(); pos++){
            if (pos+1 < str.length()){
                // 当前Character是数字，且下一个Character不是数字，才需要划分一个新的Spliterator
                if (Character.isDigit(str.charAt(pos)) && !Character.isDigit(str.charAt(pos+1))){
                    Spliterator<Character> spliterator = new NumCounterSpliterator(str.substring(currentChar, pos));
                    currentChar = pos;
                    return spliterator;
                }
            }else {
                if (Character.isDigit(str.charAt(pos))){
                    Spliterator<Character> spliterator = new NumCounterSpliterator(str.substring(currentChar, pos));
                    currentChar = pos;
                    return spliterator;
                }
            }
        }
        return null;
    }

    /**
     * 分割器可以通过方法提供剩余元素数量的估计。
     * @return
     */
    @Override
    public long estimateSize() {
        return str.length() - currentChar;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}