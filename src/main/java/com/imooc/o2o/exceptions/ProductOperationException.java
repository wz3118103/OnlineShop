package com.imooc.o2o.exceptions;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 15:03 2019/11/20
 * @Description :
 * @Modified By   :
 * @Version :
 */
public class ProductOperationException extends RuntimeException {


    private static final long serialVersionUID = 5076172298827469013L;

    /**
     *
     */
    public ProductOperationException(String msg) {
        super(msg);
    }
}
