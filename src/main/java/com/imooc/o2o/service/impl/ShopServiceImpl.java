package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ShopExecuction;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 9:51 2019/11/17
 * @Description :
 * @Modified By   :
 * @Version :
 */

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;
    /**
     * 需要事务支持
     * @param shop
     * @param shopImgInpuStream
     * @return
     */
    @Override
    @Transactional
    public ShopExecuction addShop(Shop shop, InputStream shopImgInpuStream, String fileName) throws ShopOperationException {
        // 包括对shop以及shop中area shopCategory是否为空的判断
        if (shop == null) {
            return new ShopExecuction(ShopStateEnum.NULL_SHOP);
        }

        try {
            // step1.将店铺的信息插入到数据库
            // 审核中
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum <= 0) {
                throw new ShopOperationException("店铺创建失败");
            } else {
                if (shopImgInpuStream != null) {
                    try {
                        // step2.获取店铺id，通过id创建图片存储的文件夹
                        // step3.在文件夹下面去处理图片
                        addShopImg(shop, shopImgInpuStream, fileName);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error: " + e.getMessage());
                    }
                    // step4.将图片文件夹的地址更新回数据库
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error: " + e.getMessage());
        }

        return new ShopExecuction(ShopStateEnum.CHECK, shop);
    }

    private void addShopImg(Shop shop, InputStream shopImgInpuStream, String fileName) {
        // 获取相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImgInpuStream, fileName, dest);
        shop.setShopImg(shopImgAddr);
    }
}