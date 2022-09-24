package com.krian.finance.core.service.impl;

import com.krian.finance.core.mapper.UserAccountMapper;
import com.krian.finance.core.pojo.entity.UserAccount;
import com.krian.finance.core.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Override
    public String commitCharge(BigDecimal chargeAmt, Long userId) {
        return null;
    }
}
