package com.brook.example.java8.stream.spliterator;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Spliterator 详细参考 http://blog.csdn.net/anonymousprogrammer/article/details/76034365
 */
class NumCounterSpliterator implements Spliterator<Character> {

    private String str;
    private int currentChar = 0;

    public NumCounterSpliterator(String str) {
        this.str = str;
    }

    /**
     * @param action
     * @return
     */
    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(str.charAt(currentChar++));
        return currentChar < str.length();
    }

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

    @Override
    public long estimateSize() {
        return str.length() - currentChar;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }
}