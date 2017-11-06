package com.brook.example.lock.redis;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Redis 实现分布式锁
 * <p>
 *   因为解锁操作只是做了简单的DEL KEY，如果某客户端在获得锁后执行业务的时间超过了锁的过期时间，
 *   则最后的解锁操作会误解掉其他客户端的操作。
*  <p>
 *   为解决此问题，我们在创建RedisLock对象时用本机时间戳和UUID来创建一个绝对唯一的lockValue，
 *   然后在加锁时存入此值，并在解锁前用GET取出值进行比较，如果匹配才做DEL。
 *   @see RedisLcokWithScript
 *
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/11/6
 */
@Slf4j
@Component
public class RedisLock implements DistributeLcok {

  final StringRedisTemplate stringRedisTemplate;

  public RedisLock(StringRedisTemplate stringRedisTemplate) {
    this.stringRedisTemplate = stringRedisTemplate;
  }

  private boolean doTryLock(String lockName, int lockSeconds){
    long nowTime = System.currentTimeMillis();
    long expireTime = nowTime + lockSeconds * 1000 + 1000; // 容忍不同服务器时间有1秒内的误差
    if(stringRedisTemplate.opsForValue().setIfAbsent(lockName,String.valueOf(expireTime))){
      stringRedisTemplate.expire(lockName, lockSeconds,TimeUnit.SECONDS);
      return true;
    }else{
      String currVal = stringRedisTemplate.opsForValue().get(lockName);
      if(StringUtils.hasText(currVal) && Long.parseLong(currVal) <nowTime){
        //这个锁已经过期了，可以获得
        // 如果setNX 和 expire 之间客户端发生崩溃,可能会出现以下情况
        String oldVal = stringRedisTemplate.opsForValue().getAndSet(lockName,String.valueOf
            (expireTime));
        if(Objects.equals(currVal, oldVal)){
          // 考虑多线程并发的情况，只有一个线程的设置值和当前值相同，它才有权利加锁
          stringRedisTemplate.expire(lockName, lockSeconds,TimeUnit.SECONDS);
          return true;

        }else{
          //  被别人抢占了锁(此时已经修改了lockKey中的值，不过误差很小可以忽略)
          return false;
        }

      }
    }
    return false;
  }

  @Override
  public boolean tryLock(String lockName, int expire) {
    try {
      return doTryLock(PREFIX +lockName,expire);
    } catch (Exception e) {
      log.error("Trying to get lock error is {}",e);
      return false;
    }
  }

  @Override
  public boolean tryLock(String lockName) {
      return this.tryLock(PREFIX +lockName, DEFFAULT_LOCK_TIMEOUT);
  }

  @Override
  public boolean tryLock(
      final String lockName,
      final int lockSeconds,
      final long tryIntervalMillis,
      final int maxTryCount) {
    int tryCount = 0;
    while (true) {
      if (++tryCount >= maxTryCount) {
        return false;
      }
      try {
        if (tryLock(PREFIX +lockName, lockSeconds)) {
          return true;
        }
      } catch (Exception e) {
        log.error("Try polling to get lock error is {} ", e);
        return false;
      }
      try {
        Thread.sleep(tryIntervalMillis);
      } catch (InterruptedException e) {
        log.error("Try polling to get lock, interrupted {}", e);
        return false;
      }
    }

  }


  @Override
  public void unlock(String lockName) {
    stringRedisTemplate.delete(PREFIX +lockName);
  }
}
