package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 9:50 2019/11/17
 * @Description :
 * @Modified By   :
 * @Version :
 */
public interface ShopService {
    ShopExecution addShop(Shop shop, ImageHolder image) throws ShopOperationException;

    Shop getByShopId(long shopId);

    ShopExecution modifyShop(Shop shop, ImageHolder image) throws ShopOperationException;

    /**
     *
     * @param shopCondition
     * @param pageIndex 前端只认页数，后端只认行数，所以要进行转换
     * @param pageSize
     * @return
     */
    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
}
