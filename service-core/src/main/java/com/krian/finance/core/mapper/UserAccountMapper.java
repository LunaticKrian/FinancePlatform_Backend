package com.krian.finance.core.mapper;

import com.krian.finance.core.pojo.entity.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户账户 Mapper 接口
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */

@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {

}
