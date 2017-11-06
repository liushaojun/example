package com.brook.example.lock.redis;

import java.util.Collections;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/11/6
 */
@Slf4j
public class RedisLcokWithScript{

  private final StringRedisTemplate stringRedisTemplate;
  private final String lockName;
  private final String lockValue;
  private volatile boolean locked = false;
  public RedisLcokWithScript(StringRedisTemplate stringRedisTemplate, String lockName) {
    this.stringRedisTemplate = stringRedisTemplate;
    this.lockName = lockName;
    this.lockValue = UUID.randomUUID().toString() + "." + System.currentTimeMillis();
  }
  public static RedisLcokWithScript getLock(StringRedisTemplate template, String lockName){
    return new RedisLcokWithScript(template, DistributeLcok.PREFIX +lockName);
  }


  /**
   * 使用脚本在redis服务器执行这个逻辑可以在一定程度上保证此操作的原子性
   * （即不会发生客户端在执行setNX和expire命令之间，
   * 发生崩溃或失去与服务器的连接导致expire没有得到执行,发生永久死锁）
   * <p>
   *   除非脚本在redis服务器执行时redis服务器发生崩溃，不过此种情况锁也会失效
   */
  private static final RedisScript<Boolean> SETNX_AND_EXPIRE_SCRIPT;
  /**
   * 使用脚本在redis服务器执行这个逻辑可以在一定程度上保证此操作的原子性
   * （即不会发生客户端在执行setNX和expire命令之间，
   * 发生崩溃或失去与服务器的连接导致expire没有得到执行， 发生永久死锁） <p>
   *   除非脚本在redis服务器执行时redis服务器发生崩溃，不过此种情况锁也会失效
   */
  private static final RedisScript<Boolean> DEL_IF_GET_EQUALS;

  static {
    StringBuilder sb = new StringBuilder();
    sb.append("if (redis.call('setnx', KEYS[1], ARGV[1]) == 1) then\n");
    sb.append("\tredis.call('expire', KEYS[1], tonumber(ARGV[2]))\n");
    sb.append("\treturn true\n");
    sb.append("else\n");
    sb.append("\treturn false\n");
    sb.append("end");
    SETNX_AND_EXPIRE_SCRIPT = new RedisScriptImpl<Boolean>(sb.toString(), Boolean.class);
  }

  static {
    StringBuilder sb = new StringBuilder();
    sb.append("if (redis.call('get', KEYS[1]) == ARGV[1]) then\n");
    sb.append("\tredis.call('del', KEYS[1])\n");
    sb.append("\treturn true\n");
    sb.append("else\n");
    sb.append("\treturn false\n");
    sb.append("end");
    DEL_IF_GET_EQUALS = new RedisScriptImpl<Boolean>(sb.toString(), Boolean.class);
  }

  private boolean doTryLock( int lockSeconds) throws Exception {

    if(locked) {
      throw new IllegalStateException("Already locked ! ");
    }
    String lockValue = UUID.randomUUID().toString() + "." + System.currentTimeMillis();
    return stringRedisTemplate
        .execute(SETNX_AND_EXPIRE_SCRIPT,
            Collections.singletonList(this.lockName),
            lockValue,
            String.valueOf(lockSeconds));

  }

  public boolean tryLock( int lockSeconds) {
    try {
      return doTryLock(lockSeconds);
    } catch (Exception e) {
      log.error("tryLock Error", e);
      return false;
    }
  }

  public boolean tryLock() {
    return this.tryLock(DistributeLcok.DEFFAULT_LOCK_TIMEOUT);
  }
  public boolean tryLock(
      final int lockSeconds,
      final long tryIntervalMillis,
      final int maxTryCount) {
    int tryCount = 0;
    while (true) {
      if (++tryCount >= maxTryCount) {
        // 获取锁超时
        return false;
      }
      try {
        if (doTryLock(lockSeconds)) {
          return true;
        }
      } catch (Exception e) {
        log.error("tryLock Error", e);
        return false;
      }
      try {
        Thread.sleep(tryIntervalMillis);
      } catch (InterruptedException e) {
        log.error("tryLock interrupted", e);
        return false;
      }
    }
  }

  /**
   * 解锁操作
   */
  public void unlock() {
    if (!locked) {
      throw new IllegalStateException("not locked yet!");
    }
    locked = false;
    // 忽略结果
    stringRedisTemplate.execute(DEL_IF_GET_EQUALS, Collections.singletonList(this.lockName),
        lockValue);
  }
  private static class RedisScriptImpl<T> implements RedisScript<T> {

    private final String script;
    private final String sha1;
    private final Class<T> resultType;

    public RedisScriptImpl(String script, Class<T> resultType) {
      this.script = script;
      this.sha1 = DigestUtils.sha1DigestAsHex(script);
      this.resultType = resultType;
    }

    @Override
    public String getSha1() {
      return sha1;
    }

    @Override
    public Class<T> getResultType() {
      return resultType;
    }

    @Override
    public String getScriptAsString() {
      return script;
    }
  }

}
