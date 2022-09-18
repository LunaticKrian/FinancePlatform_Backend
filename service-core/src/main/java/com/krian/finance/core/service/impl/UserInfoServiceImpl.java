package com.krian.finance.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krian.common.exception.Assert;
import com.krian.common.result.ResponseEnum;
import com.krian.common.utils.MD5;
import com.krian.finance.core.mapper.UserAccountMapper;
import com.krian.finance.core.mapper.UserInfoMapper;
import com.krian.finance.core.pojo.entity.UserAccount;
import com.krian.finance.core.pojo.entity.UserInfo;
import com.krian.finance.core.pojo.vo.RegisterVo;
import com.krian.finance.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Transactional(rollbackFor = Exception.class)  // 事务回滚
    @Override
    public void register(RegisterVo registerVo) {
        // 判断用户是否已经被注册：
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", registerVo.getMobile());
        Integer count = baseMapper.selectCount(queryWrapper);  // 判断当前手机号是否被注册
        Assert.isTrue(count == 0, ResponseEnum.MOBILE_ERROR);

        // 插入用户信息表（user_info）：
        UserInfo userInfo = new UserInfo();
        userInfo.setUserType(registerVo.getUserType());
        userInfo.setNickName(registerVo.getMobile());
        userInfo.setName(registerVo.getMobile());
        userInfo.setMobile(registerVo.getMobile());
        userInfo.setPassword(MD5.encrypt(registerVo.getPassword()));
        userInfo.setStatus(UserInfo.STATUS_NORMAL);
        userInfo.setHeadImg(UserInfo.USER_AVATAR);
        baseMapper.insert(userInfo);

        // 插入用户账户表（user_account）：
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfo.getId());
        userAccountMapper.insert(userAccount);
    }
}
