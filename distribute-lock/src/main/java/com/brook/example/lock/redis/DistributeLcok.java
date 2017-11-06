package com.brook.example.lock.redis;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/11/6
 */
public interface DistributeLcok {

  String PREFIX = "lock:";
  int DEFFAULT_LOCK_TIMEOUT = 60; //60 秒
  /**
   * 尝试获取锁
   * @param lockName 锁名称
   * @param expire 过期时间
   * @return boolean
   */
  boolean tryLock(final String lockName, final int expire);

  /**
   * 尝试获取锁
   * @param lockName 锁名称
   * @return boolean
   */
  boolean tryLock(final String lockName);

  /**
   * 尝试轮询获取锁
   * @param lockName 锁名称
   * @param lockSeconds 锁过期时间
   * @param tryIntervalMillis 轮询时间间隔
   * @param maxTryCount 最大轮询次数
   * @return boolean true 获得锁
   */
  boolean tryLock(
      final String lockName,
      final int lockSeconds,
      final long tryIntervalMillis,
      final int maxTryCount);

  /**
   * /**
   * 如果加锁后的操作比较耗时，调用方其实可以在unlock前根据时间判断下锁是否已经过期
   * 如果已经过期可以不用调用，减少一次请求
    @param lockName 锁的名称
   */
  void unlock(final String lockName);
}