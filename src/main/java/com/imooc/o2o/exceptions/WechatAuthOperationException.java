package com.imooc.o2o.exceptions;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 15:03 2019/11/20
 * @Description :
 * @Modified By   :
 * @Version :
 */
public class WechatAuthOperationException extends RuntimeException {


    private static final long serialVersionUID = -4290016045533442745L;

    /**
     *
     */
    public WechatAuthOperationException(String msg) {
        super(msg);
    }
}
