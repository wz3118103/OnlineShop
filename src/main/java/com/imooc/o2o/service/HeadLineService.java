package com.imooc.o2o.service;

import com.imooc.o2o.entity.HeadLine;

import java.util.List;

public interface HeadLineService {
    public static final String HEADLINELISTKEY = "headlinelist";

    List<HeadLine> getHeadLineList(HeadLine headLineCondition);
}
