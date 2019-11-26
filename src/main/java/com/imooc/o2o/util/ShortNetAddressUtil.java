package com.imooc.o2o.util;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 8:35 2019/11/26
 * @Description :wechat拼接的链接非常长，生成二维码时会生成失败，所以要生成等价的短链接
 * @Modified By   :
 * @Version :
 */
public class ShortNetAddressUtil {
    private static Logger logger = LoggerFactory.getLogger(ShortNetAddressUtil.class);

    final static String CREATE_API = "https://dwz.cn/admin/v2/create";
    /**
     * 设置Token，在https://dwz.cn/console/userinfo 获取自己的token，复制粘贴到这里
      */
    final static String TOKEN = "815447802372c3caec25b396d2068faa";

    /**
     * GSON弥补了JSON的许多不足的地方，在实际应用中更加适用于Java开发。
     * 利用GSON来操作java对象和json数据之间的相互转换
     */
    class UrlResponse {
        @SerializedName("Code")
        private int code;

        @SerializedName("ErrMsg")
        private String errMsg;

        @SerializedName("LongUrl")
        private String longUrl;

        @SerializedName("ShortUrl")
        private String shortUrl;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public String getLongUrl() {
            return longUrl;
        }

        public void setLongUrl(String longUrl) {
            this.longUrl = longUrl;
        }

        public String getShortUrl() {
            return shortUrl;
        }

        public void setShortUrl(String shortUrl) {
            this.shortUrl = shortUrl;
        }
    }

    /**
     * 创建短网址
     *
     * @param longUrl
     *            长网址：即原网址
     * @return  成功：短网址
     *          失败：返回空字符串
     */
    public static String getShortURL(String longUrl) {
        String params = "{\"Url\":\""+ longUrl + "\"}";

        BufferedReader reader = null;
        try {
            // 创建连接
            URL url = new URL(CREATE_API);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            // 设置请求方式
            connection.setRequestMethod("POST");
            // 设置发送数据的格式
            connection.setRequestProperty("Content-Type", "application/json");
            // 设置发送数据的格式");
            connection.setRequestProperty("Token", TOKEN);

            // 发起请求
            connection.connect();
            // utf-8编码
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.append(params);
            out.flush();
            out.close();

            // 读取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            String res = "";
            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();

            // 抽取生成短网址
            UrlResponse urlResponse = new Gson().fromJson(res, UrlResponse.class);
            if (urlResponse.getCode() == 0) {
                logger.info("short url: " + urlResponse.getShortUrl());
                return urlResponse.getShortUrl();
            } else {
                System.out.println(urlResponse.getErrMsg());
                logger.error("getshortURL error: " + urlResponse.getErrMsg());
            }

            // TODO：自定义错误信息
            return "";
        } catch (IOException e) {
            // TODO
            logger.error("getshortURL error: " + e.getMessage());
            e.printStackTrace();
        }
        // TODO：自定义错误信息
        return "";
    }

    public static void main(String[] args) {
        String res = getShortURL("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx54cccf594102a556&redirect_uri=http://119.3.135.196/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");
        System.out.println(res);
    }
}

