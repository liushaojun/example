package com.brook.example.lock.redis;

import java.util.Collections;
import java.util.Objects;
import redis.clients.jedis.Jedis;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/12/14
 */
public class RedisTool {

  private static final String SUCCESS = "OK";
  private static final long SUCCESS_FLAG = 1L;
  private static final String SET_IF_NOT_EXIST = "NX";
  private static final String SET_WITH_EXPIRE_TIME = "PX";

  /**
   * 尝试获取分布式锁
   *
   * @param jedis Redis客户端
   * @param lockKey 锁
   * @param requestId 请求标识
   * @param expireTime 超期时间
   * @return 是否获取成功
   */
  public static boolean tryLock(Jedis jedis,
      String lockKey,
      String requestId,
      int expireTime) {

    String result = jedis
        .set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
    return SUCCESS.equals(result);

  }

  /**
   * 正确解锁姿势
   *
   * @param jedis Redis客户端
   * @param lockKey 锁
   * @param requestId 请求标识
   * @return 是否释放成功
   */

  public static boolean unlock(Jedis jedis, String lockKey, String requestId) {

    String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    Object result = jedis
        .eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
    return Objects.equals(SUCCESS_FLAG, result);

  }
}
