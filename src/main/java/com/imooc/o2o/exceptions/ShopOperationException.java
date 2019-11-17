package com.imooc.o2o.exceptions;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 10:31 2019/11/17
 * @Description :
 * @Modified By   :
 * @Version :
 */
public class ShopOperationException extends RuntimeException{

    private static final long serialVersionUID = 2361446884822298905L;

    public ShopOperationException(String message) {
        super(message);
    }
}
