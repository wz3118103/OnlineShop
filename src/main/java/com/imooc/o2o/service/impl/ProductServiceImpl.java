package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 9:15 2019/11/21
 * @Description :
 * @Modified By   :
 * @Version :
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    /**
     * step1.处理缩略图，获取缩略图相对路径并赋值给product
     * step2.写入表tb_product，获取productId
     * step3.结合productId批量处理商品详情图
     * step4.将商品详情图批量插入tb_product_img
     * @param product
     * @param image
     * @param productImgList
     * @return
     * @throws ProductOperationException
     */
    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder image, List<ImageHolder> productImgList)
            throws ProductOperationException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            // 默认为上架状态
            product.setEnableStatus(1);

            // step1.处理缩略图，获取缩略图相对路径并赋值给product
            if (image != null) {
                addImage(product, image);
            }

            // step2.写入表tb_product，获取productId （mapper中ProductDao.xml获取的）
            try {
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("创建商品失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品失败： " + e.toString());
            }

            // step3.结合productId批量处理商品详情图
            // step4.将商品详情图批量插入tb_product_img
            if (productImgList != null && productImgList.size() > 0) {
                addProductImgList(product, productImgList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
        int count = productDao.queryProductCount(productCondition);
        ProductExecution pe = new ProductExecution();
        pe.setProductList(productList);
        pe.setCount(count);
        return pe;
    }

    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductById(productId);
    }

    /**
     * step1.处理缩略图，此时需要先删除原来的缩略图，获取缩略图相对路径并赋值给product
     * step2.商品详情图同样处理，需要先进行删除，并更新tb_product_img
     * step3.更新表tb_product
     *
     * @param product
     * @param image
     * @param productImgList
     * @return
     * @throws ProductOperationException
     */
    @Override
    @Transactional
    public ProductExecution modifyProduct(Product product, ImageHolder image, List<ImageHolder> productImgList) throws ProductOperationException {
        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            product.setLastEditTime(new Date());
            // step1.处理缩略图，此时需要先删除原来的缩略图（物理删除、数据库更新），获取缩略图相对路径并赋值给product
            if (image != null) {
                Product tmpProduct = productDao.queryProductById(product.getProductId());
                if (tmpProduct.getImgAddr() != null) {
                    ImageUtil.deleteFileOrDir(tmpProduct.getImgAddr());
                }
                addImage(product, image);
            }

            // step2.商品详情图同样处理，需要先进行删除，并更新tb_product_img（物理删除、数据库删除并更新）
            if (productImgList != null && productImgList.size() > 0) {
                deleteProductImgList(product.getProductId());
                addProductImgList(product, productImgList);
            }

            // step3.更新表tb_product
            try {
                int effectedNum = productDao.updateProduct(product);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("更新商品失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            } catch (Exception e) {
                throw new ProductOperationException("更新商品信息失败：" + e.toString());
            }
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }


    /**
     * 处理缩略图
     * @param product
     * @param image
     */
    private void addImage(Product product, ImageHolder image) {
        // 首先获取相对路径 /upload/item/shop/shopId/
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        // 在根路径+dest路径下生成缩略图，并返回相对路径（带文件名和扩展名）
        String imageAddr = ImageUtil.generateThumbnail(image, dest);
        product.setImgAddr(imageAddr);
    }

    private void addProductImgList(Product product, List<ImageHolder> productImgList) {
        // 首先获取相对路径 /upload/item/shop/shopId/
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> imgList = new ArrayList<>();
        for (ImageHolder image : productImgList) {
            String imgAddr = ImageUtil.generateNormalImg(image, dest);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            imgList.add(productImg);
        }
        if (productImgList.size() > 0) {
            try {
                int effectedNum = productImgDao.batchInsertProductImg(imgList);
                if (effectedNum <= 0) {
                    throw new ProductOperationException("创建商品详情图片失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品详情图片失败： " + e.toString());
            }
        }
    }

    private void deleteProductImgList(long productId) {
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        for (ProductImg img : productImgList) {
            ImageUtil.deleteFileOrDir(img.getImgAddr());
        }
        productImgDao.deleteProductImgByProductId(productId);
    }
}
