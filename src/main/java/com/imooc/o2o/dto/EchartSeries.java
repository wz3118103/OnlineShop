package com.imooc.o2o.dto;

import java.util.List;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 15:13 2019/11/26
 * @Description :迎合echart里的series项
 * @Modified By   :
 * @Version :
 */

public class EchartSeries {
    private String name;
    private String type = "bar";
    private List<Integer> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

}
