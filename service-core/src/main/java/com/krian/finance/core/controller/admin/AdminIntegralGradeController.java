package com.krian.finance.core.controller.admin;


import com.krian.common.exception.BusinessException;
import com.krian.common.result.R;
import com.krian.common.result.ResponseEnum;
import com.krian.finance.core.pojo.entity.IntegralGrade;
import com.krian.finance.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
@Api(tags = "积分等级管理")  // Swagger2 API分类标签
//@CrossOrigin  // 允许跨域请求
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {

    @Autowired
    private IntegralGradeService integralGradeService;

    /**
     * @param integralGrade
     * @return
     * @Func 新增积分等级数据
     */
    @ApiOperation(value = "新增积分等级", notes = "数据库表新增积分等级数据")
    @PostMapping("/save")
    public R save(
            @ApiParam(value = "积分等级对象", readOnly = true)
            @RequestBody IntegralGrade integralGrade) {

        // 借款额度不能为空：
        if (integralGrade.getBorrowAmount() == null){
            throw new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        }

        boolean flag = integralGradeService.save(integralGrade);
        if (flag) return R.SUCCESS().message("新增数据成功！");
        else return R.ERROR().message("新增数据失败！");
    }

    /**
     * @param id
     * @return boolean
     * @Func 逻辑删除
     */
    @ApiOperation(value = "根据ID删除数据记录", notes = "逻辑删除数据记录")  // value：标题性说明；notes：详细说明
    @DeleteMapping("/remove/{id}")
    public R removeById(
            @ApiParam(value = "数据id", example = "1", readOnly = true)  // Swagger2 @ApiParam 参数注解
            @PathVariable Long id) {  // @PathVariable 从请求参数中获取值
        boolean flag = integralGradeService.removeById(id);
        if (flag) return R.SUCCESS().message("删除数据成功！");
        else return R.ERROR().message("删除数据失败！");
    }

    /**
     * @Func 通过ID更新积分等级信息
     * @param integralGrade
     * @return
     */
    @ApiOperation(value = "通过ID更新积分等级信息")
    @PutMapping("/update/")
    public R updateById(@RequestBody IntegralGrade integralGrade){
        boolean flag = integralGradeService.updateById(integralGrade);
        if (flag) return R.SUCCESS().message("更新数据成功！");
        else return R.ERROR().message("更新数据失败！");
    }

    /**
     * @param id
     * @return
     * @Func 根据ID获取积分等级
     */
    @ApiOperation("根据ID获取积分等级")
    @GetMapping("/get/{id}")
    public R getById(
            @PathParam(value = "数据ID信息")
            @PathVariable Long id) {
        IntegralGrade integralGrade = integralGradeService.getById(id);
        if (integralGrade != null) return R.SUCCESS().message("获取积分等级信息成功！").data("record", integralGrade);
        else return R.ERROR().message("获取积分等级失败！");
    }

    /**
     * @return List<IntegralGrade>
     * @Func 获取积分列表
     */
    @ApiOperation("积分等级列表")  // Swagger2 API操作注释
    @GetMapping("/list")
    public R list() {
        // 调用Service方法获取积分等级列表信息：
        List<IntegralGrade> list = integralGradeService.list();
        return R.SUCCESS().message("获取积分等级列表成功！").data("list", list);
    }


}

