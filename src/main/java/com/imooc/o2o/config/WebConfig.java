package com.imooc.o2o.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 14:12 2019/11/26
 * @Description :
 * @Modified By   :
 * @Version :
 */

@Configuration
@ImportResource({"classpath:spring/spring-service.xml",
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-redis.xml"})
@Import(QuartzConfiguration.class)
public class WebConfig {
}
