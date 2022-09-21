package com.krian.finance.core.mapper;

import com.krian.finance.core.pojo.entity.UserBind;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户绑定表 Mapper 接口
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */

@Mapper
public interface UserBindMapper extends BaseMapper<UserBind> {

}
