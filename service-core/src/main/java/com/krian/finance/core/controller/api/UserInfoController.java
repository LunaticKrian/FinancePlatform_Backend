package com.krian.finance.core.controller.api;


import com.krian.common.exception.Assert;
import com.krian.common.result.R;
import com.krian.common.result.ResponseEnum;
import com.krian.finance.core.pojo.vo.RegisterVo;
import com.krian.finance.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
@Api(tags = "会员接口")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/core/userInfo")
public class UserInfoController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("会员注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterVo registerVo) {

        // 1.校验验证嘛是否正确：
        String codeGen = (String) redisTemplate.opsForValue().get("krian:sms:code:" + registerVo.getMobile());  // 从Redis中获取验证码(生成的验证码)
        String codeVo = registerVo.getCode();  // 获取前端提交的验证码（用户输入）
        // 进行断言比较：
        Assert.equals(codeVo, codeGen, ResponseEnum.CODE_ERROR);

        // 2.注册用户：
        userInfoService.register(registerVo);

        return R.SUCCESS().message("注册成功！");
    }
}

