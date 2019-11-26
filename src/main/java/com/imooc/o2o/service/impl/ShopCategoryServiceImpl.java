package com.imooc.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.dao.ShopCategoryDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopCategoryExecution;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopCategoryStateEnum;
import com.imooc.o2o.exceptions.ShopCategoryOperationException;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 9:49 2019/11/18
 * @Description :
 * @Modified By   :
 * @Version :
 */
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    private static Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
        String key = SHOPCATEGORYLISTKEY;
        List<ShopCategory> shopCategoryList = null;
        ObjectMapper mapper = new ObjectMapper();
        if (shopCategoryCondition == null) {
            key = key + "_allfirstlevle";
        } else if (shopCategoryCondition != null && shopCategoryCondition.getParent() != null
                && shopCategoryCondition.getParent().getShopCategoryId() != null) {
            key += "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
        } else if (shopCategoryCondition != null) {
            // 创建商店时使用，每个商店只属于二级类别
            key += "_allsecondlevel";
        }

        if (!jedisKeys.exists(key)) {
            shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
            String jsonString;
            try {
                jsonString = mapper.writeValueAsString(shopCategoryList);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ShopCategoryOperationException(e.getMessage());
            }
        } else {
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,
                    ShopCategory.class);
            try {
                shopCategoryList = mapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new  ShopCategoryOperationException(e.getMessage());
            }
        }

        return shopCategoryList;
    }

    @Override
    @Transactional
    public ShopCategoryExecution addShopCategory(ShopCategory shopCategory, ImageHolder thumbnail) {
        // 空值判断
        if (shopCategory != null) {
            // 设定默认值
            shopCategory.setCreateTime(new Date());
            shopCategory.setLastEditTime(new Date());
            if (thumbnail != null) {
                // 若上传有图片流，则进行存储操作，并给shopCategory实体类设置上相对路径
                addThumbnail(shopCategory, thumbnail);
            }
            try {
                // 往数据库添加店铺类别信息
                int effectedNum = shopCategoryDao.insertShopCategory(shopCategory);
                if (effectedNum > 0) {
                    // 删除店铺类别之前在redis里存储的一切key,for简单实现
                    deleteRedis4ShopCategory();
                    return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategory);
                } else {
                    return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new ShopCategoryOperationException("添加店铺类别信息失败:" + e.toString());
            }
        } else {
            return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public ShopCategoryExecution modifyShopCategory(ShopCategory shopCategory, ImageHolder thumbnail) {
        // 空值判断，主要判断shopCategoryId不为空
        if (shopCategory.getShopCategoryId() != null && shopCategory.getShopCategoryId() > 0) {
            // 设定默认值
            shopCategory.setLastEditTime(new Date());
            if (thumbnail != null) {
                // 若上传的图片不为空，则先获取之前的图片路径
                ShopCategory tempShopCategory = shopCategoryDao.queryShopCategoryById(shopCategory.getShopCategoryId());
                if (tempShopCategory.getShopCategoryImg() != null) {
                    // 若之前图片不为空，则先移除之前的图片
                    ImageUtil.deleteFileOrDir(tempShopCategory.getShopCategoryImg());
                }
                // 存储新的图片
                addThumbnail(shopCategory, thumbnail);
            }
            try {
                // 更新数据库信息
                int effectedNum = shopCategoryDao.updateShopCategory(shopCategory);
                if (effectedNum > 0) {
                    // 删除店铺类别之前在redis里存储的一切key,for简单实现
                    deleteRedis4ShopCategory();
                    return new ShopCategoryExecution(ShopCategoryStateEnum.SUCCESS, shopCategory);
                } else {
                    return new ShopCategoryExecution(ShopCategoryStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new ShopCategoryOperationException("更新店铺类别信息失败:" + e.toString());
            }
        } else {
            return new ShopCategoryExecution(ShopCategoryStateEnum.EMPTY);
        }
    }

    /**
     * 存储图片
     *
     * @param shopCategory
     * @param thumbnail
     */
    private void addThumbnail(ShopCategory shopCategory, ImageHolder thumbnail) {
        String dest = PathUtil.getShopCategoryPath();
        String thumbnailAddr = ImageUtil.generateNormalImg(thumbnail, dest);
        shopCategory.setShopCategoryImg(thumbnailAddr);
    }

    /**
     * 移除跟实体类相关的redis key-value
     */
    private void deleteRedis4ShopCategory() {
        String prefix = SHOPCATEGORYLISTKEY;
        // 获取跟店铺类别相关的redis key
        Set<String> keySet = jedisKeys.keys(prefix + "*");
        for (String key : keySet) {
            // 逐条删除
            jedisKeys.del(key);
        }
    }

    @Override
    public ShopCategory getShopCategoryById(Long shopCategoryId) {
        return shopCategoryDao.queryShopCategoryById(shopCategoryId);
    }
}
