package com.krian.finance.core.service;

import com.krian.finance.core.pojo.entity.UserInfo;
import com.krian.finance.core.pojo.query.UserInfoQuery;
import com.krian.finance.core.pojo.vo.LoginVO;
import com.krian.finance.core.pojo.vo.RegisterVO;
import com.krian.finance.core.pojo.vo.UserIndexVO;
import com.krian.finance.core.pojo.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
public interface UserInfoService extends IService<UserInfo> {

    void register(RegisterVO registerVO);

    UserInfoVO login(LoginVO loginVO, String ip);


    IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery);

    void lock(Long id, Integer status);

    boolean checkMobile(String mobile);

    UserIndexVO getIndexUserInfo(Long userId);

    String getMobileByBindCode(String bindCode);
}
