package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Shop;

public interface ShopDao {
    Shop queryByShopId(long shopId);
    /**
     * 新增店铺
     * @param shop
     * @return 影响的行数，返回1表示成果，返回-1失败（MyBatis返回）
     */
    int insertShop(Shop shop);

    int updateShop(Shop shop);
}
