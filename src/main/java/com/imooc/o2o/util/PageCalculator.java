package com.imooc.o2o.util;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 8:46 2019/11/20
 * @Description :
 * @Modified By   :
 * @Version :
 */
public class PageCalculator {
    public static int calculateRowIndex(int pageIndex, int pageSize) {
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
    }
}
