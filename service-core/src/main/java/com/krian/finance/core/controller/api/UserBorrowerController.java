package com.krian.finance.core.controller.api;


import com.krian.common.result.R;
import com.krian.finance.base.utils.JwtUtils;
import com.krian.finance.core.pojo.vo.BorrowerVo;
import com.krian.finance.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */

@Api(tags = "借款人")
@Slf4j
@RestController
@RequestMapping("/api/core/borrower")
public class UserBorrowerController {

    @Autowired
    private BorrowerService borrowerService;

    @ApiOperation("保存借款人信息")
    @PostMapping("/auth/save")  // 必须是已登录的情况才能进行相关操作
    public R save(@RequestBody BorrowerVo borrowerVo, HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);

        borrowerService.saveBorrowerVoByUserId(borrowerVo, userId);
        return R.SUCCESS().message("信息提交成功！");
    }

    @ApiOperation("获取借款人认证状态")
    @GetMapping("/auth/getBorrowerStatus")
    public R getBorrowerStatus(HttpServletRequest request){
        // 从请求中获取token：
        String token = request.getHeader("token");
        // 从token中解析出UserId：
        Long userId = JwtUtils.getUserId(token);
        Integer status = borrowerService.getStatusByUserId(userId);
        return R.SUCCESS().data("borrowerStatus", status);
    }
}

