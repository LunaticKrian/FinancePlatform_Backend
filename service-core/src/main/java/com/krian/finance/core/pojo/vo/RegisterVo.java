package com.krian.finance.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

// 用户注册的VO对象，接收和封装前端提交的表单数据
@Data
@ApiModel(description = "用户注册对象")
public class RegisterVo {

    @ApiModelProperty(value = "用户类型")
    private Integer userType;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "验证嘛")
    private String code;

    @ApiModelProperty(value = "密码")
    private String password;
}
