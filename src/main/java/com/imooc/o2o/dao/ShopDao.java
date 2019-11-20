package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {
    Shop queryByShopId(long shopId);
    /**
     * 新增店铺
     * @param shop
     * @return 影响的行数，返回1表示成果，返回-1失败（MyBatis返回）
     */
    int insertShop(Shop shop);

    int updateShop(Shop shop);


    /**
     * 分页查询商店
     *   可输入条件：
     *   1）商店名（模糊）
     *   2）商店状态
     *   3）商店类别
     *   4）区域Id
     *   5）owner
     * @param ShopCondition
     * @param rowIndex 从第rowIndex行取数据
     * @param pageSize 取多少行数据
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition")Shop ShopCondition,
                             @Param("rowIndex") int rowIndex,
                             @Param("pageSize") int pageSize);

    /**
     * 与queryShopList配套，返回符合查询条件的商店总数
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition")Shop shopCondition);
}
