package com.krian.finance.core.service.impl;

import com.krian.finance.core.mapper.UserBindMapper;
import com.krian.finance.core.pojo.entity.UserBind;
import com.krian.finance.core.service.UserBindService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
@Service
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {

}
