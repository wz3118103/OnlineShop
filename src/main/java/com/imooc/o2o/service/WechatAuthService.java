package com.imooc.o2o.service;

import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.exceptions.WechatAuthOperationException;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 15:32 2019/11/24
 * @Description :
 * @Modified By   :
 * @Version :
 */
public interface WechatAuthService {

    /**
     * 通过openId查找平台对应的微信帐号
     *
     * @param openId
     * @return
     */
    WechatAuth getWechatAuthByOpenId(String openId);

    /**
     * 注册本平台的微信帐号
     *
     * @param wechatAuth
     * @return
     * @throws WechatAuthOperationException
     */
    WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;

}

