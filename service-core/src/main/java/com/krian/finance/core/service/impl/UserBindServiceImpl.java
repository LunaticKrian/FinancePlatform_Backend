package com.krian.finance.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krian.common.exception.Assert;
import com.krian.common.result.ResponseEnum;
import com.krian.finance.core.bank.BankConst;
import com.krian.finance.core.bank.FormHelper;
import com.krian.finance.core.bank.RequestHelper;
import com.krian.finance.core.enums.UserBindEnum;
import com.krian.finance.core.mapper.UserBindMapper;
import com.krian.finance.core.mapper.UserInfoMapper;
import com.krian.finance.core.pojo.entity.UserBind;
import com.krian.finance.core.pojo.entity.UserInfo;
import com.krian.finance.core.pojo.vo.UserBindVo;
import com.krian.finance.core.service.UserBindService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public String commitBindUser(UserBindVo userBindVo, Long userId) {

        // 不同的user_id, 相同的身份证，如果存在，则不允许
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper
                .eq("id_card", userBindVo.getIdCard())
                .ne("user_id", userId);
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);
        Assert.isNull(userBind, ResponseEnum.USER_BIND_IDCARD_EXIST_ERROR);

        // 用户是否曾经填写过绑定表单
        userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id", userId);
        userBind = baseMapper.selectOne(userBindQueryWrapper);

        if(userBind == null){
            // 创建用户绑定记录
            userBind = new UserBind();
            BeanUtils.copyProperties(userBindVo, userBind);
            userBind.setUserId(userId);
            userBind.setStatus(UserBindEnum.NO_BIND.getStatus());
            baseMapper.insert(userBind);
        }else{
            // 相同的user_id，如果存在，那么就取出数据，做更新
            BeanUtils.copyProperties(userBindVo, userBind);
            baseMapper.updateById(userBind);
        }

        // 组装自动提交表单的参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", BankConst.AGENT_ID);
        paramMap.put("agentUserId", userId);
        paramMap.put("idCard",userBindVo.getIdCard());
        paramMap.put("personalName", userBindVo.getName());
        paramMap.put("bankType", userBindVo.getBankType());
        paramMap.put("bankNo", userBindVo.getBankNo());
        paramMap.put("mobile", userBindVo.getMobile());
        paramMap.put("returnUrl", BankConst.USERBIND_RETURN_URL);
        paramMap.put("notifyUrl", BankConst.USERBIND_NOTIFY_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        paramMap.put("sign", RequestHelper.getSign(paramMap));

        // 生成动态表单字符串
        String formStr = FormHelper.buildForm(BankConst.USERBIND_URL, paramMap);
        return formStr;
    }

    @Override
    public void notify(Map<String, Object> paramMap) {
        String bindCode = (String)paramMap.get("bindCode");
        String agentUserId = (String)paramMap.get("agentUserId");

        //根据user_id查询user_bind记录
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id", agentUserId);

        //更新用户绑定表
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);
        userBind.setBindCode(bindCode);
        userBind.setStatus(UserBindEnum.BIND_OK.getStatus());
        baseMapper.updateById(userBind);

        //更新用户表
        UserInfo userInfo = userInfoMapper.selectById(agentUserId);
        userInfo.setBindCode(bindCode);
        userInfo.setName(userBind.getName());
        userInfo.setIdCard(userBind.getIdCard());
        userInfo.setBindStatus(UserBindEnum.BIND_OK.getStatus());
        userInfoMapper.updateById(userInfo);
    }
}
