package com.imooc.o2o.service;

import com.imooc.o2o.entity.PersonInfo;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 15:21 2019/11/24
 * @Description :
 * @Modified By   :
 * @Version :
 */
public interface PersonInfoService {
    /**
     * 根据用户Id获取personInfo信息
     * @param userId
     * @return
     */
    PersonInfo getPersonInfoById(Long userId);
}
