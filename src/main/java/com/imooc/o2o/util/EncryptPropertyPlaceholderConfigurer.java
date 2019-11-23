package com.imooc.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 9:15 2019/11/23
 * @Description :
 * @Modified By   :
 * @Version :
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    /**
     * 需要解密的字段数组
      */
    private String[] decryptPropertyNames = {"jdbc.username", "jdbc.password"};

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProperty(propertyName)) {
            String decryptValue = DESUtil.getDecryptString(propertyValue);
            return decryptValue;
        } else {
            return propertyValue;
        }
    }



    private boolean isEncryptProperty(String propertyName) {
        for (String property : decryptPropertyNames) {
            if (property.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
}
