package com.imooc.o2o.service;

public interface CacheService {
    /**
     * 依据keyPrefix删除匹配该模式下的所有key-value
     * @param keyPrefix
     */
    void removeFromCache(String keyPrefix);
}
