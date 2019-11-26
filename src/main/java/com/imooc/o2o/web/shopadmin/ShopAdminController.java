package com.imooc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 22:37 2019/11/17
 * @Description :
 * @Modified By   :
 * @Version :
 */

@Controller
@RequestMapping(value = "shopadmin", method = {RequestMethod.GET})
public class ShopAdminController {
    @RequestMapping(value = "/shopoperation")
    public String shopOperation(){
        // 为什么只设置了中间路径，因为前缀和后缀在spring-web.xml中有设置
        return "shop/shopoperation";
    }

    @RequestMapping(value = "/shoplist")
    public String shopList(){
        // 为什么只设置了中间路径，因为前缀和后缀在spring-web.xml中有设置
        return "shop/shoplist";
    }

    @RequestMapping(value = "/shopmanagement")
    public String shopManagement(){
        // 为什么只设置了中间路径，因为前缀和后缀在spring-web.xml中有设置
        return "shop/shopmanagement";
    }

    @RequestMapping(value = "/productcategorymanagement")
    public String productCategoryManagement(){
        // 为什么只设置了中间路径，因为前缀和后缀在spring-web.xml中有设置
        return "shop/productcategorymanagement";
    }

    @RequestMapping(value = "/productoperation")
    public String productOperation(){
        return "shop/productoperation";
    }

    @RequestMapping(value = "/productmanagement")
    public String productManagement(){
        return "shop/productmanagement";
    }

    @RequestMapping(value = "/shopauthmanagement")
    public String shopAuthManagement() {
        // 转发至店铺授权页面
        return "shop/shopauthmanagement";
    }

    @RequestMapping(value = "/shopauthedit")
    public String shopAuthEdit() {
        // 转发至授权信息修改页面
        return "shop/shopauthedit";
    }

}
