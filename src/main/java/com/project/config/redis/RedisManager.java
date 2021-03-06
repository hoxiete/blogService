package com.project.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisManager {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    //=============================common============================
    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     */
    public void expire(String key,long time){
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }
    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     */
    public void expire(String key,long time,TimeUnit unit){
        redisTemplate.expire(key, time, unit);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String ... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 批量删除key
     * @param keys
     */
    public void del(Collection keys){
        redisTemplate.delete(keys);
    }

    //============================String=============================
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public String getString(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Set<Object> keys(String key){
        return redisTemplate.keys(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     */
    public void set(String key,Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     */
    public void setString(String key,String value,long time) {
        stringRedisTemplate.opsForValue().set(key, value, time,TimeUnit.SECONDS);
    }

    /**
     * 普通缓存放入List
     * @param key 键
     * @param value 值
     */
    public void pushToList(String key,Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 普通缓存放入List
     * @param key 键
     */
    public List<Object> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    public void set(String key,Object value,long time){
        if(time>0){
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        }else{
            set(key, value);
        }
    }

    /**
     * 使用scan命令 查询某些前缀的key
     * @param key
     * @return
     */
    public Set<String> scan(String key){
        Set<String> execute = this.stringRedisTemplate.execute(new RedisCallback<Set<String>>() {

            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {

                Set<String> binaryKeys = new HashSet<>();

                Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(key).count(1000).build());
                while (cursor.hasNext()) {
                    binaryKeys.add(new String(cursor.next()));
                }
                return binaryKeys;
            }
        });
        return execute;
    }

    /**
     * 使用scan命令 查询某些前缀的key 有多少个
     * 用来获取当前session数量,也就是在线用户
     * @param key
     * @return
     */
    public Long scanSize(String key){
        long dbSize = this.stringRedisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long count = 0L;
                Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(key).count(1000).build());
                while (cursor.hasNext()) {
                    cursor.next();
                    count++;
                }
                return count;
            }
        });
        return dbSize;
    }
}

