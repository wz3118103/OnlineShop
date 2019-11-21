package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {
    /**
     * 查询商品列表并分页
     * 查询条件：
     * 1）商品名（模糊）
     * 2）商品状态
     * 3）商店Id
     * 4）商品类别
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<Product> queryProductList(@Param("productCondition") Product productCondition,
                                   @Param("rowIndex") int rowIndex,
                                   @Param("pageSize") int pageSize);

    int queryProductCount(@Param("productCondition") Product productCondition);

    Product queryProductById(long productId);

    int insertProduct(Product product);

    int updateProduct(Product product);

    /**
     * 删除商品类别之前，将商品类别ID置为空
     *
     * @param productCategoryId
     * @return
     */
    int updateProductCategoryToNull(long productCategoryId);

    int deleteProduct(@Param("productId") long productId, @Param("shopId") long shopId);




}
