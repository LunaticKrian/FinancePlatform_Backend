package com.krian.finance.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krian.common.exception.Assert;
import com.krian.common.result.ResponseEnum;
import com.krian.common.utils.MD5;
import com.krian.finance.base.utils.JwtUtils;
import com.krian.finance.core.mapper.UserAccountMapper;
import com.krian.finance.core.mapper.UserInfoMapper;
import com.krian.finance.core.mapper.UserLoginRecordMapper;
import com.krian.finance.core.pojo.entity.UserAccount;
import com.krian.finance.core.pojo.entity.UserInfo;
import com.krian.finance.core.pojo.entity.UserLoginRecord;
import com.krian.finance.core.pojo.query.UserInfoQuery;
import com.krian.finance.core.pojo.vo.LoginVo;
import com.krian.finance.core.pojo.vo.RegisterVo;
import com.krian.finance.core.pojo.vo.UserInfoVo;
import com.krian.finance.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

    @Autowired
    private UserLoginRecordMapper userLoginRecordMapper;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoVo login(LoginVo loginVo, String ip) {
        // 获取前端传递的参数：
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        Integer userType = loginVo.getUserType();

        // 判断用户是否存在：
        // 1.创建查询条件构造器，查询数据库中是否包含记录：
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("mobile", mobile)
                .eq("user_type", userType);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        // 2.断言从数据库中获取的UserInfo对象不为null：
        Assert.notNull(userInfo, ResponseEnum.LOGIN_MOBILE_ERROR);

        // 判断密码是否正确：
        Assert.equals(MD5.encrypt(password), userInfo.getPassword(), ResponseEnum.LOGIN_PASSWORD_ERROR);

        // 断言用户是否被禁用：
        Assert.equals(userInfo.getStatus(), UserInfo.STATUS_NORMAL, ResponseEnum.LOGIN_LOKED_ERROR);

        // 记录登录日志：
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);
        userLoginRecordMapper.insert(userLoginRecord);

        // 生成token：
        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getName());

        // 组装UserInfoVo：
        UserInfoVo userInfoVO = new UserInfoVo();
        userInfoVO.setToken(token);
        userInfoVO.setName(userInfo.getName());
        userInfoVO.setNickName(userInfo.getNickName());
        userInfoVO.setHeadImg(userInfo.getHeadImg());
        userInfoVO.setMobile(mobile);
        userInfoVO.setUserType(userType);

        // 返回：
        return userInfoVO;
    }

    @Override
    public IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery) {
        if(userInfoQuery == null){
            return baseMapper.selectPage(pageParam, null);
        }

        String mobile = userInfoQuery.getMobile();
        Integer status = userInfoQuery.getStatus();
        Integer userType = userInfoQuery.getUserType();

        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper
                .eq(StringUtils.isNotBlank(mobile), "mobile", mobile)
                .eq(status != null, "status", status)
                .eq(userType != null, "user_type", userType);


        return baseMapper.selectPage(pageParam, userInfoQueryWrapper);
    }

    @Override
    public void lock(Long id, Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);
    }

    @Override
    public boolean checkMobile(String mobile) {
        // 查询数据库，当前传递的mobile是否存在：
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }
}
