package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 23:04 2019/11/21
 * @Description :
 * @Modified By   :
 * @Version :
 */
@Controller
@RequestMapping("/frontend")
public class FrontendController {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    private String index() {
        return "frontend/index";
    }

    @RequestMapping(value = "/shoplist", method = RequestMethod.GET)
    private String getShopList() {
        return "frontend/shoplist";
    }

    @RequestMapping(value = "/shopdetail", method = RequestMethod.GET)
    private String getShopDetail() {
        return "frontend/shopdetail";
    }

    @RequestMapping(value = "/productdetail", method = RequestMethod.GET)
    private String getProductDetail() {
        return "frontend/productdetail";
    }
}
