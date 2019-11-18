package com.imooc.o2o.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 13:44 2019/11/18
 * @Description :校验验证码
 * @Modified By   :
 * @Version :
 */
public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request) {
        String verifyCodeExpected = (String)request.getSession().getAttribute(
                Constants.KAPTCHA_SESSION_KEY);
        String verifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
        if (verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)) {
            return false;
        }
        return true;
    }
}
