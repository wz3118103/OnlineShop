package com.imooc.o2o.service;

import com.imooc.o2o.entity.HeadLine;

import java.util.List;

public interface HeadLineService {
    List<HeadLine> getHeadLineList(HeadLine headLineCondition);
}
