package com.imooc.o2o.dto;

import java.util.HashSet;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 15:14 2019/11/26
 * @Description :迎合echart里的xAxis项
 * @Modified By   :
 * @Version :
 */
public class EchartXAxis {
    private String type = "category";
    /**
     * 为了去重
     */
    private HashSet<String> data;

    public HashSet<String> getData() {
        return data;
    }

    public void setData(HashSet<String> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

}