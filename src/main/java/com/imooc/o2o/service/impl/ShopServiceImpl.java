package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopAuthMapDao;
import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 9:51 2019/11/17
 * @Description :
 * @Modified By   :
 * @Version :
 */

@Service
public class ShopServiceImpl implements ShopService {
    private final static Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;
    /**
     * 需要事务支持
     * @param shop
     * @param image
     * @return
     */
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder image) throws ShopOperationException {
        // 包括对shop以及shop中area shopCategory是否为空的判断
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
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
                if (image.getImage() != null) {
                    try {
                        // step2.获取店铺id，通过id创建图片存储的文件夹
                        // step3.在文件夹下面去处理图片
                        addShopImg(shop, image);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error: " + e.getMessage());
                    }
                    // step4.将图片文件夹的地址更新回数据库
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败");
                    }

                    // step5.执行增加shopAuthMap操作
                    ShopAuthMap shopAuthMap = new ShopAuthMap();
                    shopAuthMap.setEmployee(shop.getOwner());
                    shopAuthMap.setShop(shop);
                    shopAuthMap.setTitle("店家");
                    shopAuthMap.setTitleFlag(0);
                    shopAuthMap.setCreateTime(new Date());
                    shopAuthMap.setLastEditTime(new Date());
                    shopAuthMap.setEnableStatus(1);
                    try {
                        effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                        if (effectedNum <= 0) {
                            logger.error("addShop:授权创建失败");
                            throw new ShopOperationException("授权创建失败");
                        }
                    } catch (Exception e) {
                        logger.error("insertShopAuthMap error: " + e.getMessage());
                        throw new ShopOperationException("授权创建失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error: " + e.getMessage());
        }

        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution modifyShop(Shop shop, ImageHolder image) throws ShopOperationException {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        // step1.判断是否需要处理图片（需要删除旧图片）
        try {
            if (image.getImage() != null && image.getImageName() != null && !"".equals(image.getImageName())) {
                Shop tmpShop = shopDao.queryByShopId(shop.getShopId());
                if (tmpShop.getShopImg() != null) {
                    ImageUtil.deleteFileOrDir(tmpShop.getShopImg());
                }
                addShopImg(shop, image);
            }
            // step2.更新店铺信息
            shop.setLastEditTime(new Date());
            int effectedNum = shopDao.updateShop(shop);
            if (effectedNum <= 0) {
                return new ShopExecution(ShopStateEnum.INNER_ERROR);
            }
            shop = shopDao.queryByShopId(shop.getShopId());
            return new ShopExecution(ShopStateEnum.SUCCESS, shop);
        } catch (Exception e) {
            throw new ShopOperationException("modify error: " + e.getMessage());
        }
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        // 前端只认页数，后端只认行数，所以要进行转换
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList != null) {
            se.setShopList(shopList);
            se.setCount(count);
        } else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    private void addShopImg(Shop shop, ImageHolder image) {
        // 获取相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(image, dest);
        shop.setShopImg(shopImgAddr);
    }


}
