package com.imooc.o2o.service;

import com.imooc.o2o.dto.ShopExecuction;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

import java.io.InputStream;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 9:50 2019/11/17
 * @Description :
 * @Modified By   :
 * @Version :
 */
public interface ShopService {
    ShopExecuction addShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;
}
