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
}
