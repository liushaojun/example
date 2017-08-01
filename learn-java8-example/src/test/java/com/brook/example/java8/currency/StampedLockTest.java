package com.brook.example.java8.currency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.StampedLock;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * StampedLock
 * <p>
 * java8 新出的读写锁，比{@link java.util.concurrent.locks.ReentrantReadWriteLock}性能更好。<br>
 * {@code ReentrantReadWriteLock} 在沒有任何读写锁时，才可以取得写入锁，
 * 这可用于实现了悲观读取（Pessimistic Reading），即如果执行中进行读取时，
 * 经常可能有另一执行要写入的需求，为了保持同步， {@code ReentrantReadWriteLock} 的读取锁定就可派上用场。
 * 然而，如果读取执行情况很多，写入很少的情况下，使用 {@code ReentrantReadWriteLock} 可能
 * 会使写入线程遭遇饥饿（Starvation）问题，也就是写入线程迟迟无法竞争到锁定而一直处于等待状态。
 * {@code StampedLock}控制锁有三种模式: <code>写</code>，<code>读</code>，<code>乐观读</code>。
 * 一个{@code StampedLock} 状态是由版本和模式两个部分组成，锁获取方法返回一个数字作为票据
 * <code>stamp</code>，它用相应的锁状态表示并控制访问，数字0表示没有写锁被授权访问。<p>
 * 在读锁上分为悲观锁和乐观锁。
 * <p>
 *
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/30
 */
public class StampedLockTest {

    @Test
    void deposit(){
        AccountWithSampedLock account = new AccountWithSampedLock();
        account.deposit(1000d);
        double amount = account.getBalance();
        assertEquals(1000d,amount);
    }
}

class AccountWithSampedLock {
    private final StampedLock lock = new StampedLock();
    private double balance;

    void deposit(double amount) {
        long stamp = lock.writeLock();
        try {
            balance = balance + amount;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    double getBalance() {
        long stamp = lock.readLock();
        try {
            return balance;
        } finally {
            lock.unlockRead(stamp);
        }
    }
}

/**
 * java doc提供的StampedLock一个例子
 */
class Point {
    private double x, y;
    private final StampedLock sl = new StampedLock();

    void move(double deltaX, double deltaY) {
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    //乐观读锁案例
    double distanceFromOrigin() {
        long stamp = sl.tryOptimisticRead(); //获得一个乐观读锁
        double currentX = x, currentY = y; //将两个字段读入本地局部变量
        if (!sl.validate(stamp)) { //检查发出乐观读锁后同时是否有其他写锁发生？
            stamp = sl.readLock(); //如果没有，我们再次获得一个读悲观锁
            try {
                currentX = x; // 将两个字段读入本地局部变量
                currentY = y; // 将两个字段读入本地局部变量
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    //悲观读锁案例
    void moveIfAtOrigin(double newX, double newY) {
        // upgrade
        // Could instead start with optimistic, not read mode
        long stamp = sl.readLock();
        try {
            while (x == 0.0 && y == 0.0) { //循环，检查当前状态是否符合
                long ws = sl.tryConvertToWriteLock(stamp); //将读锁转为写锁
                if (ws != 0L) {
                    // 成功转为写锁
                    stamp = ws; //如果成功 替换票据
                    x = newX; //进行状态改变
                    y = newY; //进行状态改变
                    break;
                } else {
                    sl.unlockRead(stamp); //我们显式释放读锁
                    stamp = sl.writeLock(); //显式直接进行写锁 然后再通过循环再试
                }
            }
        } finally {
            sl.unlock(stamp); // 释放读写锁
        }
    }
}