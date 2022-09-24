package com.krian.finance.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.krian.common.result.R;
import com.krian.finance.base.utils.JwtUtils;
import com.krian.finance.core.service.UserAccountService;
import com.krian.finance.core.bank.RequestHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Api(tags = "会员账户")
@RestController
@RequestMapping("/api/core/userAccount")
@Slf4j
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @ApiOperation("充值")
    @PostMapping("/auth/commitCharge/{chargeAmt}")
    public R commitCharge(
            @ApiParam(value = "充值金额", required = true)
            @PathVariable BigDecimal chargeAmt, HttpServletRequest request) {

        //获取当前登录用户的id
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        //组装表单字符串，用于远程提交数据
        String formStr = userAccountService.commitCharge(chargeAmt, userId);
        return R.SUCCESS().data("formStr", formStr);
    }
//
//    @ApiOperation(value = "用户充值异步回调")
//    @PostMapping("/notify")
//    public String notify(HttpServletRequest request) {
//        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
//        log.info("用户充值异步回调：" + JSON.toJSONString(paramMap));
//
//        //验签
//        if (RequestHelper.isSignEquals(paramMap)) {
//
//            //判断业务是否成功
//            if ("0001".equals(paramMap.get("resultCode"))) {
//                //同步账户数据
//                return userAccountService.notify(paramMap);
//            } else {
//                return "success";
//            }
//
//        } else {
//            return "fail";
//        }
//    }

}
