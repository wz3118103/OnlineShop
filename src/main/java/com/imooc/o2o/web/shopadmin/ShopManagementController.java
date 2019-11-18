package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ShopExecuction;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 15:06 2019/11/17
 * @Description :实现店铺管理的逻辑
 * @Modified By   :
 * @Version :
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    /**
     * 所有商店都放在二级商店类别下面，也即返回的是parent不为空的商店类别
     * @return
     */
    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList;
        List<Area> areaList;
        try {
            // 当传入一个空的ShopCategory时，此时获取的是所有parent不为空的商店类别，也即二级商店类别
            // 也即商店真正挂载的类别
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success",true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }


    /**
     *
     * @param request 客户端请求，http请求中的所有信息都封装在参数中
     * @return
     */
    @RequestMapping(value = "registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object>  registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        // step1.接收并转换相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        // request中是否有上传的文件流
        if (resolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
            shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        // step2.注册店铺
        if (shop != null && shopImg != null) {
            // 假设前端传过来的信息是不可靠的，越少依赖越好，所以owner一般从会话中获取
            // Session TODO
            PersonInfo owner = new PersonInfo();
            owner.setUserId(1L);
            shop.setOwner(owner);
//            String fileName = PathUtil.getImgBasePath() + ImageUtil.getRandomFileName();
//            File shopImgFile = null;
//            try {
//                shopImgFile = inputStreamTofile(shopImg.getInputStream(), fileName);
//            } catch (IOException e) {
//                modelMap.put("success", false);
//                modelMap.put("errMsg", e.getMessage());
//                return modelMap;
//            }
            ShopExecuction se = null;
            try {
                se = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
            if (se.getState() == ShopStateEnum.CHECK.getState()) {
                modelMap.put("success", true);
            } else  {
                modelMap.put("success", false);
                modelMap.put("errMsg", se.getStateInfo());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }

/*    private static File inputStreamTofile(InputStream ins, String fileName) {
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("文件创建异常：" + e.getMessage());
        }
        try (FileOutputStream os = new FileOutputStream(file)){
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = ins.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            return file;
        } catch (Exception e) {
            throw new RuntimeException("调用inputStreamTofile异常：" + e.getMessage());
        } finally {
            try {
                if (ins != null) {
                    ins.close();
                }
            }  catch (IOException e) {
                throw new RuntimeException("关闭IO异常：" + e.getMessage());
            }
        }
    }*/
}
