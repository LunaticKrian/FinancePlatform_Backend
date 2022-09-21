package com.krian.finance.core.mapper;

import com.krian.finance.core.pojo.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户基本信息 Mapper 接口
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
