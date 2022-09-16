package com.krian.service.core;

import com.google.errorprone.annotations.Var;
import com.krian.finance.core.ServiceCoreApplication;
import com.krian.finance.core.mapper.DictMapper;
import com.krian.finance.core.pojo.entity.Dict;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = ServiceCoreApplication.class)
@RunWith(SpringRunner.class)
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DictMapper dictMapper;

    @Test
    public void saveDict(){
        Dict dict = dictMapper.selectById(1);

        // 调用RedisTemplate 将字符串存入Redis数据库：
        redisTemplate.opsForValue().set("dict", dict,5, TimeUnit.MINUTES);  // 设置了缓存过期时间
    }

    @Test
    public void getDict(){
        Dict dict = (Dict) redisTemplate.opsForValue().get("dict");
        log.info(String.valueOf(dict));
    }

}
