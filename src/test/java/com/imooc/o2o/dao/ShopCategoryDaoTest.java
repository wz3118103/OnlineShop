package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 9:22 2019/11/18
 * @Description :
 * @Modified By   :
 * @Version :
 */
public class ShopCategoryDaoTest extends BaseTest {
    @Autowired
    ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryShopCategory() {
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(new ShopCategory());
        assertEquals(21, shopCategoryList.size());

        // 测试商店类别为10下面的所有店铺类别
        ShopCategory category = new ShopCategory();
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(10L);
        category.setParent(parent);
        shopCategoryList = shopCategoryDao.queryShopCategory(category);
        assertEquals(2, shopCategoryList.size());
        for (ShopCategory shopCategory : shopCategoryList) {
            System.out.println(shopCategory.getShopCategoryName());
        }
    }

    @Test
    public void testQueryOneLevelShopCategory() {
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(null);
        System.out.println(shopCategoryList.size());
    }
}
