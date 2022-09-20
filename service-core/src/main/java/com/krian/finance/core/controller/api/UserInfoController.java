package com.krian.finance.core.controller.api;


import com.krian.common.exception.Assert;
import com.krian.common.result.R;
import com.krian.common.result.ResponseEnum;
import com.krian.common.utils.RegexValidateUtils;
import com.krian.finance.base.utils.JwtUtils;
import com.krian.finance.core.pojo.vo.LoginVo;
import com.krian.finance.core.pojo.vo.RegisterVo;
import com.krian.finance.core.pojo.vo.UserInfoVo;
import com.krian.finance.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
//@CrossOrigin
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
        // 数据校验：
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(password, ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notEmpty(code, ResponseEnum.CODE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);

        // 1.校验验证嘛是否正确：
        String codeGen = (String) redisTemplate.opsForValue().get("krian:sms:code:" + registerVo.getMobile());  // 从Redis中获取验证码(生成的验证码)
        String codeVo = registerVo.getCode();  // 获取前端提交的验证码（用户输入）
        // 进行断言比较：
        Assert.equals(codeVo, codeGen, ResponseEnum.CODE_ERROR);

        // 2.注册用户：
        userInfoService.register(registerVo);

        return R.SUCCESS().message("注册成功！");
    }

    @ApiOperation("会员登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginVo loginVo, HttpServletRequest request){
        // 获取会员登录信息：
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        // 参数校验：
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_ERROR);
        Assert.notEmpty(password, ResponseEnum.PASSWORD_NULL_ERROR);

        String ip = request.getRemoteAddr();  // 获取当前请求的IP地址
        UserInfoVo userInfoVo = userInfoService.login(loginVo, ip);

        return R.SUCCESS().data("userInfo", userInfoVo);
    }

    @ApiOperation("校验令牌")
    @GetMapping("/checkToken")
    public R checkToken(HttpServletRequest request) {

        String token = request.getHeader("token");
        boolean result = JwtUtils.checkToken(token);

        if(result){
            return R.SUCCESS();
        }else{
            return R.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }

    @ApiOperation("校验手机号是否被注册")
    @GetMapping("/checkMobile/{mobile}")
    public boolean checkMobile(@PathVariable String mobile){
        return userInfoService.checkMobile(mobile);
    }
}

