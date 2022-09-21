package com.krian.finance.core.service;

import com.krian.finance.core.pojo.entity.UserBind;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krian.finance.core.pojo.vo.UserBindVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */

public interface UserBindService extends IService<UserBind> {

    String commitBindUser(UserBindVo userBindVo, Long userId);

    void notify(Map<String, Object> paramMap);

}
