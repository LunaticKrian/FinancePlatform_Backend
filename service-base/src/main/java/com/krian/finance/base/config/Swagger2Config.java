package com.krian.finance.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// Swagger 配置类
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket adminApiConfig() {  // Docket 文档对象
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")  // API 分组管理
                .apiInfo(adminApiInfo())
                .select()  // 设置分组过滤器
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }

    // API文档信息配置：
    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("交易平台后台管理系统API文档")
                .description("本文档描述资金交易平台后台系统的各个模块的接口调用方式")
                .version("1.0")
                .contact(new Contact("krian", "http://krian.com", "krian@qq.com"))  // 联系人
                .build();
    }

    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/api/.*")))
                .build();
    }

    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder()
                .title("交易平台后台管理系统API文档")
                .description("本文档描述资金交易平台后台系统的各个模块的接口调用方式")
                .version("1.0")
                .contact(new Contact("krian", "http://krian.com", "krian@qq.com"))  // 联系人
                .build();
    }
}
