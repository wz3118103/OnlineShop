package com.imooc.o2o.service;

import com.imooc.o2o.entity.ShopCategory;

import java.util.List;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 9:32 2019/11/18
 * @Description :
 * @Modified By   :
 * @Version :
 */
public interface ShopCategoryService {
    public static final String SHOPCATEGORYLISTKEY = "shopcategorylist";
    /**
     *
     * @param shopCategoryCondition 查询某个店铺类别下的所有子类别
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
