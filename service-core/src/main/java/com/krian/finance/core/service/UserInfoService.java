package com.krian.finance.core.service;

import com.krian.finance.core.pojo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krian.finance.core.pojo.vo.LoginVo;
import com.krian.finance.core.pojo.vo.RegisterVo;
import com.krian.finance.core.pojo.vo.UserInfoVo;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
public interface UserInfoService extends IService<UserInfo> {

    void register(RegisterVo registerVo);

    UserInfoVo login(LoginVo loginVo, String ip);
}
