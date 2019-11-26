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

    /**
     * 店铺的奖品列表页路由
     *
     * @return
     */
    @RequestMapping(value = "/awardlist", method = RequestMethod.GET)
    private String showAwardList() {
        return "frontend/awardlist";
    }

    /**
     * 奖品兑换列表页路由
     *
     * @return
     */
    @RequestMapping(value = "/pointrecord", method = RequestMethod.GET)
    private String showPointRecord() {
        return "frontend/pointrecord";
    }

    /**
     * 奖品详情页路由
     *
     * @return
     */
    @RequestMapping(value = "/myawarddetail", method = RequestMethod.GET)
    private String showMyAwardDetail() {
        return "frontend/myawarddetail";
    }

    /**
     * 消费记录列表页路由
     *
     * @return
     */
    @RequestMapping(value = "/myrecord", method = RequestMethod.GET)
    private String showMyRecord() {
        return "frontend/myrecord";
    }

    /**
     * 用户各店铺积分信息页路由
     *
     * @return
     */
    @RequestMapping(value = "/mypoint", method = RequestMethod.GET)
    private String showMyPoint() {
        return "frontend/mypoint";
    }
}
