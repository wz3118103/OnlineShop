package com.imooc.o2o.dao;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 15:39 2019/11/24
 * @Description :
 * @Modified By   :
 * @Version :
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WechatAuthDaoTest extends BaseTest {
    @Autowired
    private WechatAuthDao wechatAuthDao;

    @Test
    public void testAInsertWechatAuth() throws Exception {
        // 新增一条微信帐号
        WechatAuth wechatAuth = new WechatAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);
        // 给微信帐号绑定上用户信息
        wechatAuth.setPersonInfo(personInfo);
        // 随意设置上openId
        wechatAuth.setOpenId("dafahizhfdhaih");
        wechatAuth.setCreateTime(new Date());
        int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testBQueryWechatAuthByOpenId() throws Exception {
        WechatAuth wechatAuth = wechatAuthDao.queryWechatInfoByOpenId("dafahizhfdhaih");
        assertEquals("测试", wechatAuth.getPersonInfo().getName());
    }

}

