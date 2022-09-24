package com.krian.finance.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krian.finance.core.enums.BorrowerStatusEnum;
import com.krian.finance.core.enums.IntegralEnum;
import com.krian.finance.core.mapper.BorrowerAttachMapper;
import com.krian.finance.core.mapper.BorrowerMapper;
import com.krian.finance.core.mapper.UserInfoMapper;
import com.krian.finance.core.mapper.UserIntegralMapper;
import com.krian.finance.core.pojo.entity.BorrowerAttach;
import com.krian.finance.core.pojo.entity.UserInfo;
import com.krian.finance.core.pojo.entity.UserIntegral;
import com.krian.finance.core.pojo.vo.BorrowerApprovalVo;
import com.krian.finance.core.pojo.vo.BorrowerAttachVo;
import com.krian.finance.core.pojo.vo.BorrowerDetailVo;
import com.krian.finance.core.pojo.vo.BorrowerVo;
import com.krian.finance.core.service.BorrowerAttachService;
import com.krian.finance.core.service.BorrowerService;
import com.krian.finance.core.pojo.entity.Borrower;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krian.finance.core.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
@Service
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private BorrowerAttachMapper borrowerAttachMapper;

    @Autowired
    private BorrowerAttachService borrowerAttachService;

    @Autowired
    private DictService dictService;

    @Autowired
    private UserIntegralMapper userIntegralMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBorrowerVoByUserId(BorrowerVo borrowerVo, Long userId) {
        // 获取用户基本信息
        UserInfo userInfo = userInfoMapper.selectById(userId);

        // 保存借款人信息
        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVo, borrower);
        borrower.setUserId(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus()); //认证中
        baseMapper.insert(borrower);

        // 保存附件
        List<BorrowerAttach> borrowerAttachList = borrowerVo.getBorrowerAttachList();
        borrowerAttachList.forEach(borrowerAttach -> {
            borrowerAttach.setBorrowerId(borrower.getId());
            borrowerAttachMapper.insert(borrowerAttach);
        });

        // 更新userInfo中的借款人认证状态
        userInfo.setBorrowAuthStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public Integer getStatusByUserId(Long userId) {

        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.select("status").eq("user_id", userId);
        List<Object> objects = baseMapper.selectObjs(borrowerQueryWrapper);
        if (objects.size() == 0) {
            return BorrowerStatusEnum.NO_AUTH.getStatus();
        }

        Integer status = (Integer) objects.get(0);
        return status;
    }

    @Override
    public IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return baseMapper.selectPage(pageParam, null);
        }

        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper
                .like("name", keyword)
                .or().like("id_card", keyword)
                .or().like("mobile", keyword)
                .orderByDesc("id");

        return baseMapper.selectPage(pageParam, borrowerQueryWrapper);
    }

    @Override
    public BorrowerDetailVo getBorrowerDetailVOById(Long id) {

        //获取借款人信息
        Borrower borrower = baseMapper.selectById(id);

        //填充基本借款人信息
        BorrowerDetailVo borrowerDetailVo = new BorrowerDetailVo();
        BeanUtils.copyProperties(borrower, borrowerDetailVo);

        //婚否
        borrowerDetailVo.setMarry(borrower.getMarry() ? "是" : "否");
        //性别
        borrowerDetailVo.setSex(borrower.getSex() == 1 ? "男" : "女");

        //下拉列表
        borrowerDetailVo.setEducation(dictService.getNameByParentDictCodeAndValue("education", borrower.getEducation()));
        borrowerDetailVo.setIndustry(dictService.getNameByParentDictCodeAndValue("industry", borrower.getIndustry()));
        borrowerDetailVo.setIncome(dictService.getNameByParentDictCodeAndValue("income", borrower.getIncome()));
        borrowerDetailVo.setReturnSource(dictService.getNameByParentDictCodeAndValue("returnSource", borrower.getReturnSource()));
        borrowerDetailVo.setContactsRelation(dictService.getNameByParentDictCodeAndValue("relation", borrower.getContactsRelation()));

        //审批状态
        String status = BorrowerStatusEnum.getMsgByStatus(borrower.getStatus());
        borrowerDetailVo.setStatus(status);

        //附件列表
        List<BorrowerAttachVo> borrowerAttachVOList = borrowerAttachService.selectBorrowerAttachVOList(id);
        borrowerDetailVo.setBorrowerAttachVOList(borrowerAttachVOList);

        return borrowerDetailVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approval(BorrowerApprovalVo borrowerApprovalVo) {

        //获取借款额度申请id
        Long borrowerId = borrowerApprovalVo.getBorrowerId();

        //获取借款额度申请对象
        Borrower borrower = baseMapper.selectById(borrowerId);

        //设置审核状态
        borrower.setStatus(borrowerApprovalVo.getStatus());
        baseMapper.updateById(borrower);

        //获取用户id
        Long userId = borrower.getUserId();

        //获取用户对象
        UserInfo userInfo = userInfoMapper.selectById(userId);

        //用户的原始积分
        Integer integral = userInfo.getIntegral();

        //计算基本信息积分
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userId);
        userIntegral.setIntegral(borrowerApprovalVo.getInfoIntegral());
        userIntegral.setContent("借款人基本信息");
        userIntegralMapper.insert(userIntegral);
        int currentIntegral = integral + borrowerApprovalVo.getInfoIntegral();

        //身份证积分
        if (borrowerApprovalVo.getIsIdCardOk()) {
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_IDCARD.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_IDCARD.getMsg());
            userIntegralMapper.insert(userIntegral);
            currentIntegral += IntegralEnum.BORROWER_IDCARD.getIntegral();
        }

        //房产积分
        if (borrowerApprovalVo.getIsHouseOk()) {
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_HOUSE.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_HOUSE.getMsg());
            userIntegralMapper.insert(userIntegral);
            currentIntegral += IntegralEnum.BORROWER_HOUSE.getIntegral();
        }

        //车辆积分
        if (borrowerApprovalVo.getIsCarOk()) {
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_CAR.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_CAR.getMsg());
            userIntegralMapper.insert(userIntegral);
            currentIntegral += IntegralEnum.BORROWER_CAR.getIntegral();
        }

        //设置用户总积分
        userInfo.setIntegral(currentIntegral);

        //修改审核状态
        userInfo.setBorrowAuthStatus(borrowerApprovalVo.getStatus());

        //更新userInfo
        userInfoMapper.updateById(userInfo);
    }
}
