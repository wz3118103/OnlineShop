package com.imooc.o2o.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 8:07 2019/11/24
 * @Description :
 * @Modified By   :
 * @Version :
 */
public class JedisPoolWriper {
    private JedisPool jedisPool;

    public JedisPoolWriper(final JedisPoolConfig poolConfig, final String host, final int port,
                           final int timeout, final String password) {
        try {
            jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
