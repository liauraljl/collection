package com.ljl.note.collection.liveRecord.common;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.lang.Nullable;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@SuppressWarnings("unchecked")
public class RedisService {

    /**
     * 强制解锁时间设置
     */
    private static final long LOCK_TIME = 2L;

    /**
     * 等待时间
     **/
    private static final long WAIT_TIME = 3L;

    /**
     * 休眠时间
     **/
    private static final long SLEEP_TIME = 100;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @Resource(name = "stringRedisTemplate")
    private RedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Set> redisScript;

    @Autowired
    private RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<Set>();
        redisScript.setResultType(Set.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis-zset-diff.lua")));
    }

    /**
     * 设置hash值
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean putHash(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
        return true;
    }

    /**
     * 删除hash 的 field值
     *
     * @param key
     * @param field
     * @return
     */
    public boolean deleteHashField(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
        return true;
    }

    /**
     * 获取hash 的 field值
     *
     * @param key
     * @param field
     * @return
     */
    public List<String> findHashField(String key, String field) {
        return JSON.parseArray(redisTemplate.opsForHash().get(key, field).toString(),String.class);
    }

    /**
     * 获取hash中的所有值
     *
     * @param key
     * @return
     */
    public List<String> findAllValuesInHash(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 获取hash中的所有key
     *
     * @param key
     * @return
     */
    public Set<String> findAllKeysInHash(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 添加数据到zset中
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public boolean addZSet(String key, String value, double score) {
        return stringRedisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 判断数据在不在zset中
     *
     * @param key
     * @param value
     * @return
     */
    public boolean isInZSet(String key, String value) {
//        return userInZSet(key, value).intValue() > 0;
        return null != userInZSetRank(key, value);
    }

    /**
     * 获取当前值在zset里面的分数
     *
     * @param key
     * @param value
     * @return
     */
    public Double userInZSet(String key, String value) {
        return stringRedisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 获取当前值在zset里面的排序
     *
     * @param key
     * @param value
     * @return
     */
    public Long userInZSetRank(String key, String value) {
        return stringRedisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 根据score获取ZSET值 带score
     *
     * @param key
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> findZSetValuesWithScoreByScore(String key, double scoreMin, double scoreMax, long start, long end) {
        return stringRedisTemplate.opsForZSet().rangeByScoreWithScores(key, scoreMin, scoreMax,start, end);
    }

    /**
     * 根据score获取ZSET值 带score  倒序
     *
     * @param key
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> findZSetValuesWithScoreByScoreReverse(String key, double scoreMin, double scoreMax, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, scoreMin, scoreMax,start, end);
    }

    /**
     * 获取ZSET值   不带score
     *
     * @param key
     * @return
     */
    public Set<String> findZSetValues(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 根据score获取ZSET值   不带score
     *
     * @param key
     * @return
     */
    public Set<String> findZSetValuesByScore(String key, double scoreMin,double scoreMax,long start, long end) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key, scoreMin, scoreMax, start, end);
    }

    /**
     * 查询两个ZSET的差集
     *
     * @param key1 小集合
     * @param key2 大集合
     * @return
     */
    public Set<String> twoZSETDiff(String key1, String key2) {
        return (Set<String>) stringRedisTemplate.execute(redisScript, Lists.newArrayList(key1, key2), "diffResult");
    }

    /**
     * 获取分布式锁
     *
     * @param key
     * @return
     */
    public boolean tryLock(String key) throws InterruptedException {
        RLock rLock = redissonClient.getLock(key);
        //tryLock，第一个参数是等待时间。 第二个参数 强制锁释放时间
        return rLock.tryLock(WAIT_TIME, LOCK_TIME, TimeUnit.SECONDS);
    }

    /**
     * 解锁
     *
     * @param key
     */
    public void unLock(String key) {
        RLock lock = redissonClient.getLock(key);
        lock.unlock();
    }

    /**
     * 获取锁，一直等待到取到锁后返回
     *
     * @param key
     * @throws InterruptedException
     */
    public boolean getLock(String key) throws InterruptedException {
        while (true) {
            if (tryLock(key)) {
                return true;
            } else {
                Thread.sleep(SLEEP_TIME);
            }
        }
    }

    public boolean hasKey(String key){
        return redisTemplate.hasKey(key)||stringRedisTemplate.hasKey(key);
    }

    public void set(String key,String value){
        stringRedisTemplate.opsForValue().set(key,value);
    }

    public void set(String key,String value,long time){
        stringRedisTemplate.opsForValue().set(key,value,time,TimeUnit.SECONDS);
    }

    public boolean delKey(String key){
        return stringRedisTemplate.delete(key);
    }

    /**
     * 获取所有KEY的Zset数据的size
     * @param keys
     * @return
     */
    public List<Long> findKeysSize(final List<String> keys) {
        return stringRedisTemplate.executePipelined(new RedisCallback<Long>() {
            @Nullable
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                for (String key : keys) {
                    connection.zSetCommands().zCard(key.getBytes());
                }
                return null;
            }
        });
    }

    /**
     * 获取所有KEY的Zset数据
     * @param keys
     * @return
     */
    public List<Set<ZSetOperations.TypedTuple<String>>> findKeysValues(final List<String> keys, final double minScore, final double maxScore) {
        return stringRedisTemplate.executePipelined(new RedisCallback<Set<ZSetOperations.TypedTuple<String>>>() {
            @Nullable
            @Override
            public Set<ZSetOperations.TypedTuple<String>> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                for (String key : keys) {
                    connection.zSetCommands().zRangeByScoreWithScores(key.getBytes(),minScore,maxScore);
                }
                return null;
            }
        });
    }

    /**
     * 获取前缀相同的所有key
     * @param pattern
     * @return
     */
    public Set<String> scan(String pattern) {
        return (Set<String>)redisTemplate.execute((RedisCallback<Set<String>>) redisConnection -> {
            Set<String> binaryKeys = new HashSet<>();
            Cursor<byte[]> cursor = redisConnection.scan(new ScanOptions.ScanOptionsBuilder().match(pattern).count(Integer.MAX_VALUE).build());
            while (cursor.hasNext()) {
                binaryKeys.add(new String(cursor.next()));
            }
            redisConnection.closePipeline();
            return binaryKeys;
        });
    }

    /**
     * 批量删除key
     * @param keys
     * @return
     */
    public void deleteKeys(final Set<String> keys) {
        stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
            @Nullable
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                for (String key : keys) {
                    connection.del(key.getBytes());
                }
                connection.closePipeline();
                return null;
            }
        });
    }
}
