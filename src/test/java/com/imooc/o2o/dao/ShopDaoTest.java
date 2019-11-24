package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopDaoTest extends BaseTest {
    @Autowired
    private ShopDao shopDao;

    @Test
    public void testInsertShop() {
        Shop shop = new Shop();

        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(10L);
        shop.setOwner(owner);
        shop.setShopCategory(shopCategory);
        shop.setArea(area);

        shop.setShopName("测试店铺-主从测试");
        shop.setShopDesc("test2主从测试");
        shop.setShopAddr("test2主从测试");
        shop.setPhone("test主从测试");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(0);
        shop.setAdvice("审核中");
        int effectedNums = shopDao.insertShop(shop);
        assertEquals(1, effectedNums);
    }

    @Test
    public void testUpdateShop() {
        Shop shop = new Shop();
        shop.setShopId(36L);

        shop.setShopDesc("测试更新描述");
        shop.setShopAddr("测试更新地址");
        shop.setLastEditTime(new Date());

        int effectedNums = shopDao.updateShop(shop);
        assertEquals(1, effectedNums);
    }

    @Test
    public void testQueryByShopId() {
        long shopId = 1L;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println("areaId: " + shop.getArea().getAreaId());
        System.out.println("areaName: " + shop.getArea().getAreaName());
    }

    @Test
    public void testQueryShopCount() {
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        int count = shopDao.queryShopCount(shopCondition);
        System.out.println("商店总数：" + count);
    }

    @Test
    public void testQueryShopList() {
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(1L);
        shopCondition.setOwner(owner);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 5);
        System.out.println("分页大小：" + shopList.size());

        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(10L);
        shopCondition.setShopCategory(sc);
        shopList = shopDao.queryShopList(shopCondition, 0, 5);
        System.out.println("分页大小：" + shopList.size());
    }

    /**
     * 查询一级类别下的所有商店列表
     */
    @Test
    public void testQueryShopListUnderOneLevel() {
        ShopCategory child = new ShopCategory();
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(12L);
        child.setParent(parent);
        Shop shopCondition = new Shop();
        shopCondition.setShopCategory(child);

        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 10);
        System.out.println("分页大小：" + shopList.size());

        int count = shopDao.queryShopCount(shopCondition);
        System.out.println("商店总数：" + count);
    }


}
