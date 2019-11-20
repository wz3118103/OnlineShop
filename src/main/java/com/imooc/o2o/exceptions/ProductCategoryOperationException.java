package com.imooc.o2o.exceptions;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 15:03 2019/11/20
 * @Description :
 * @Modified By   :
 * @Version :
 */
public class ProductCategoryOperationException extends RuntimeException {

    private static final long serialVersionUID = 1182563719599527969L;

    /**
     *
     */
    public ProductCategoryOperationException(String msg) {
        super(msg);
    }
}
