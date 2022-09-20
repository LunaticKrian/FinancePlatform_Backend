package com.krian.finance.base.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenFeignConfig {
    @Bean
    Logger.Level feignLoggerLevel(){
        // 设置OpenFeign的日志级别：
        return Logger.Level.FULL;
    }
}
