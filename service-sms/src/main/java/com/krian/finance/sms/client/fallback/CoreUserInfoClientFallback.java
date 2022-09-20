package com.krian.finance.sms.client.fallback;

import com.krian.finance.sms.client.CoreUserInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CoreUserInfoClientFallback implements CoreUserInfoClient {
    @Override
    public boolean checkMobile(String mobile) {
        log.info("远程调用失败，服务熔断！");
        // 手机号不重复：
        return false;
    }
}
