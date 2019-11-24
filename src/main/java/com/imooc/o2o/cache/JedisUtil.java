package com.imooc.o2o.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import java.util.Set;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 8:10 2019/11/24
 * @Description :
 * @Modified By   :
 * @Version :
 */
public class JedisUtil {
    /**
     * 操作Key的方法
     */
    public Keys KEYS;
    /**
     * 对存储结构为String类型的操作
     */
    public Strings STRINGS;
    /**
     * Redis连接池对象
     */
    private JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPoolWriper jedisPoolWriper) {
        this.jedisPool = jedisPoolWriper.getJedisPool();
    }

    /**
     * 从jedis连接池中获取jedis对象
     * @return
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }


    public class Keys {
        /**
         * 清空所有key
         * @return
         */
        public String flushAll() {
            Jedis jedis = getJedis();
            String data = jedis.flushAll();
            jedis.close();
            return data;
        }

        /**
         * 删除keys对应的记录，可以是多个key
         * @param keys
         * @return
         */
        public long del(String... keys) {
            Jedis jedis = getJedis();
            long count = jedis.del(keys);
            jedis.close();
            return count;
        }

        /**
         * 判断key是否存在
         * @param key
         * @return
         */
        public boolean exists(String key) {
            Jedis jedis = getJedis();
            boolean isExist = jedis.exists(key);
            jedis.close();
            return isExist;
        }

        /**
         * 查找所有匹配给定模式的键
         * @param pattern
         * @return
         */
        public Set<String> keys(String pattern) {
            Jedis jedis = getJedis();
            Set<String> set = jedis.keys(pattern);
            jedis.close();
            return set;
        }
    }


    public class Strings {
        /**
         * 根据key获取记录
         * @param key
         * @return
         */
        public String get(String key) {
            Jedis jedis = getJedis();
            String value = jedis.get(key);
            jedis.close();
            return value;
        }

        /**
         * 添加记录，如果记录已存在则覆盖
         * @param key
         * @param value
         * @return
         */
        public String set(String key, String value) {
            return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }

        /**
         * 添加记录，如果记录已存在将覆盖原有的value
         * @param key
         * @param value
         * @return
         */
        public String set(byte[] key, byte[] value) {
            Jedis jedis = getJedis();
            String status = jedis.set(key, value);
            jedis.close();
            return status;
        }
    }
}
