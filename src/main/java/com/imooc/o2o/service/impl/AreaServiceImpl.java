package com.imooc.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.dao.AreaDao;
import com.imooc.o2o.dto.AreaExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.enums.AreaStateEnum;
import com.imooc.o2o.exceptions.AreaOperationException;
import com.imooc.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Override
    @Transactional
    public List<Area> getAreaList() {
        String key = AREALISTKEY;
        List<Area> areaList = null;
        ObjectMapper mapper = new ObjectMapper();
        if (!jedisKeys.exists(key)) {
            areaList = areaDao.queryArea();
            String jsonString;
            try {
                jsonString = mapper.writeValueAsString(areaList);
                jedisStrings.set(key, jsonString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
        } else {
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class,
                    Area.class);
            try {
                areaList = mapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new AreaOperationException(e.getMessage());
            }
        }
        return areaList;
    }


    @Override
    public AreaExecution addArea(Area area) {
        // 空值判断，主要是判断areaName不为空
        if (area.getAreaName() != null && !"".equals(area.getAreaName())) {
            // 设置默认值
            area.setCreateTime(new Date());
            area.setLastEditTime(new Date());
            try {
                int effectedNum = areaDao.insertArea(area);
                if (effectedNum > 0) {
                    deleteRedis4Area();
                    return new AreaExecution(AreaStateEnum.SUCCESS, area);
                } else {
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new AreaOperationException("添加区域信息失败:" + e.toString());
            }
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }

    @Override
    public AreaExecution modifyArea(Area area) {
        // 空值判断，主要是areaId不为空
        if (area.getAreaId() != null && area.getAreaId() > 0) {
            // 设置默认值
            area.setLastEditTime(new Date());
            try {
                // 更新区域信息
                int effectedNum = areaDao.updateArea(area);
                if (effectedNum > 0) {
                    deleteRedis4Area();
                    return new AreaExecution(AreaStateEnum.SUCCESS, area);
                } else {
                    return new AreaExecution(AreaStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new AreaOperationException("更新区域信息失败:" + e.toString());
            }
        } else {
            return new AreaExecution(AreaStateEnum.EMPTY);
        }
    }

    /**
     * 移除跟实体类相关的redis key-value
     */
    private void deleteRedis4Area() {
        String key = AREALISTKEY;
        // 若redis存在对应的key,则将key清除
        if (jedisKeys.exists(key)) {
            jedisKeys.del(key);
        }
    }

}
