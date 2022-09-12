package com.krian.finance.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@MapperScan({"com.krian.finance"})  // 配置包扫描器
@ComponentScan(value = "com.krian.finance")  // 配置组件扫描
//@EnableSwagger2  // 开启Swagger2 API文档2
public class ServiceCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCoreApplication.class, args);
    }
}
