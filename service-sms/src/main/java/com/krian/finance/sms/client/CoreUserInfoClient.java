package com.krian.finance.sms.client;

import com.krian.finance.sms.client.fallback.CoreUserInfoClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 远程调用客户端方法,fallback：服务降级，熔断处理：
@FeignClient(value = "service-core", fallback = CoreUserInfoClientFallback.class)  // 指定远程调用微服务的名称（配置文件中配置的项目模块名称）
public interface CoreUserInfoClient {

    // 调用service-core模块中的接口方法：
    @GetMapping("/api/core/userInfo/checkMobile/{mobile}")
    boolean checkMobile(@PathVariable String mobile);

}
