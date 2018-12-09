package com.finance.redis.mq;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class DistributedLock {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    private static final long INTERVAL_TIMES = 200; // 下一次重试等待，单位毫秒
    private static final String LOCK_MSG = "OK";
    private static final Long UNLOCK_MSG = 1L;

    private final JedisPool jedisPool; // redis连接池
    private final String lockKey; // lock Key
    private final long lockExpiryInMillis; // 锁的过期时长，单位毫秒

    private final ThreadLocal<Lock> lockThreadLocal = new ThreadLocal<>();

    /**
     * 构造方法
     *
     * @param jedisPool          redis连接池
     * @param lockKey            锁的Key
     * @param lockExpiryInMillis 锁的过期时长，单位毫秒
     */
    public DistributedLock(JedisPool jedisPool, String lockKey, long lockExpiryInMillis) {
        this.jedisPool = jedisPool;
        this.lockKey = lockKey;
        this.lockExpiryInMillis = lockExpiryInMillis;
    }

    /**
     * 构造方法
     * <p>
     * 使用锁默认的过期时长Integer.MAX_VALUE，即锁永远不会过期
     *
     * @param jedisPool redis连接池
     * @param lockKey   锁的Key
     */
    public DistributedLock(JedisPool jedisPool, String lockKey) {
        this(jedisPool, lockKey, Integer.MAX_VALUE);
    }

    public static void main(String[] args) throws InterruptedException {
        JedisPool jedisPool = new JedisPool(); // init
        DistributedLock lock = new DistributedLock(jedisPool, "lock_" + UUID.randomUUID().toString());
        boolean acquireResult = false;
        try {
            acquireResult = lock.acquire(Integer.MAX_VALUE);
            if (!acquireResult) {
                System.out.println("busy");
            }
            // do sth...
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (acquireResult) {
                boolean releaseResult = lock.release();
                if (!releaseResult) {
                    System.out.println("释放锁失败！");
                }
            }
        }

    }

    /**
     * 获取锁在redis中的Key标记
     *
     * @return locks key
     */
    public String getLockKey() {
        return this.lockKey;
    }

    /**
     * 锁的过期时长
     */
    public long getLockExpiryInMillis() {
        return lockExpiryInMillis;
    }

    private String nextUid() {
        return UUID.randomUUID().toString();
    }

    private synchronized Jedis getClient() {
        return jedisPool.getResource();
    }

    private synchronized void closeClient(Jedis jedis) {
        jedis.close();
    }

    /**
     * 请求分布式锁，不会阻塞，直接返回
     *
     * @param jedis redis 连接
     * @return 成功获取锁返回true, 否则返回false
     */
    private boolean tryAcquire(Jedis jedis) {
        final Lock nLock = new Lock(nextUid());
        String result = jedis.set(this.lockKey, nLock.toString(), "NX", "PX", this.lockExpiryInMillis);
        if (LOCK_MSG.equals(result)) {
            lockThreadLocal.set(nLock);
            return true;
        }
        return false;
    }

    /**
     * 请求分布式锁，不会阻塞，直接返回
     *
     * @return 成功获取锁返回true, 否则返回false
     */
    public boolean tryAcquire() {
        Jedis jedis = null;
        try {
            jedis = getClient();
            return tryAcquire(jedis);
        } finally {
            if (jedis != null) {
                closeClient(jedis);
            }
        }
    }

    /**
     * 超时请求分布式锁，会阻塞
     *
     * 采用"自旋获取锁"的方式，直至获取锁成功或者请求锁超时
     *
     * @param acquireTimeoutInMillis 锁的请求超时时长
     */
    public boolean acquire(long acquireTimeoutInMillis) throws InterruptedException {
        Jedis jedis = null;
        try {
            jedis = getClient();
            while (acquireTimeoutInMillis >= 0) {
                boolean result = tryAcquire(jedis);
                if (result) { // 获取锁成功直接返回，否则循环重试
                    return true;
                }
                acquireTimeoutInMillis -= INTERVAL_TIMES;
                Thread.sleep(INTERVAL_TIMES);
            }
        } finally {
            if (jedis != null) {
                closeClient(jedis);
            }
        }
        return false;
    }

    /**
     * 释放锁
     */
    public boolean release() throws InterruptedException {
        return release(0);
    }

    /**
     * 释放锁
     */
    public boolean release(long releaseTimeoutInMillis) throws InterruptedException {
        Jedis jedis = null;
        try {
            jedis = getClient();
            return release(jedis, releaseTimeoutInMillis);
        } finally {
            if (jedis != null) {
                closeClient(jedis);
            }
        }
    }

    /**
     * 释放锁
     */
    private boolean release(Jedis jedis, long releaseTimeoutInMillis) throws InterruptedException {
        Lock cLock = lockThreadLocal.get();
        if (cLock == null) {
            logger.info("lock is null");
        } else {
            // 不使用固定的字符串作为键的值，而是设置一个不可猜测（non-guessable）的长随机字符串，作为口令串（token）；
            // 不使用 DEL 命令来释放锁，而是发送一个 Lua 脚本，这个脚本只在客户端传入的值和键的口令串相匹配时，才对键进行删除；
            // 可以防止持有过期锁的客户端误删现有锁的情况出现。
            // 如在 key 超时之后业务并没有执行完毕但却自动释放锁了，仍会导致并发问题。
            final String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

            while (releaseTimeoutInMillis >= 0) {
                Object result = jedis.eval(luaScript, Collections.singletonList(this.lockKey), Collections.singletonList(cLock.toString()));
                if (UNLOCK_MSG.equals(result)) {
                    lockThreadLocal.remove();
                    return true;
                }
                releaseTimeoutInMillis -= INTERVAL_TIMES;
                Thread.sleep(INTERVAL_TIMES);
            }
        }
        return false;
    }

    /**
     * 锁
     */
    protected static class Lock {

        private String uid; // lock 唯一标识

        Lock(String uid) {
            this.uid = uid;
        }

        public String getUid() {
            return uid;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this, false);
        }
    }

}
