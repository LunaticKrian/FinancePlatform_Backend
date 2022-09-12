package com.krian.finance.core.service.impl;

import com.krian.finance.core.mapper.UserLoginRecordMapper;
import com.krian.finance.core.pojo.entity.UserLoginRecord;
import com.krian.finance.core.service.UserLoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {

}
