package com.imooc.o2o.service;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecuction;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 10:35 2019/11/17
 * @Description :
 * @Modified By   :
 * @Version :
 */
public class ShopServiceTest extends BaseTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testAddShop() throws FileNotFoundException {
        Shop shop = new Shop();

        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(3);
        shopCategory.setShopCategoryId(10L);
        shop.setOwner(owner);
        shop.setShopCategory(shopCategory);
        shop.setArea(area);

        shop.setShopName("测试店铺3-主从测试");
        shop.setShopDesc("test3-主从测试");
        shop.setShopAddr("test3-主从测试");
        shop.setPhone("test3-主从测试");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");

        File shopImg = new File("E:/wz/image/xiaohuangren.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder image = new ImageHolder(shopImg.getName(), is);
        ShopExecuction se = shopService.addShop(shop, image);
        assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
    }

    @Test
    public void testModifyShop() throws FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(52L);
        shop.setShopName("修改后的店铺名称");
        File shopImg = new File("E:/wz/image/dabai.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder image = new ImageHolder("dabai.jpg", is);
        ShopExecuction se = shopService.modifyShop(shop, image);
        System.out.println("更新图片地址： " + se.getShop().getShopImg());
    }

    @Test
    public void testGetShopList() {
        Shop shopCondition = new Shop();
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(10L);
        shopCondition.setShopCategory(sc);
        ShopExecuction se = shopService.getShopList(shopCondition, 3, 2);
        System.out.println("分页大小：" +se.getShopList().size());
        System.out.println("商店总数：" +se.getCount());
    }
}
