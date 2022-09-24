package com.krian.finance.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krian.finance.core.enums.LendStatusEnum;
import com.krian.finance.core.mapper.BorrowerMapper;
import com.krian.finance.core.mapper.LendMapper;
import com.krian.finance.core.pojo.entity.BorrowInfo;
import com.krian.finance.core.pojo.entity.Borrower;
import com.krian.finance.core.pojo.entity.Lend;
import com.krian.finance.core.pojo.vo.BorrowInfoApprovalVo;
import com.krian.finance.core.pojo.vo.BorrowerDetailVo;
import com.krian.finance.core.service.BorrowerService;
import com.krian.finance.core.service.DictService;
import com.krian.finance.core.service.LendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krian.finance.core.utils.LendNoUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
@Service
public class LendServiceImpl extends ServiceImpl<LendMapper, Lend> implements LendService {


    @Resource
    private DictService dictService;

    @Resource
    private BorrowerMapper borrowerMapper;

    @Resource
    private BorrowerService borrowerService;

    @Override
    public void createLend(BorrowInfoApprovalVo borrowInfoApprovalVo, BorrowInfo borrowInfo) {
        Lend lend = new Lend();
        lend.setUserId(borrowInfo.getUserId());
        lend.setBorrowInfoId(borrowInfo.getId());
        lend.setLendNo(LendNoUtils.getLendNo());
        lend.setTitle(borrowInfoApprovalVo.getTitle());
        lend.setAmount(borrowInfo.getAmount()); //标的金额
        lend.setPeriod(borrowInfo.getPeriod());
        lend.setLendYearRate(borrowInfoApprovalVo.getLendYearRate().divide(new BigDecimal(100)));
        lend.setServiceRate(borrowInfoApprovalVo.getServiceRate().divide(new BigDecimal(100)));
        lend.setReturnMethod(borrowInfo.getReturnMethod());
        lend.setLowestAmount(new BigDecimal(100)); //最低投资金额
        lend.setInvestAmount(new BigDecimal(0)); //已投金额
        lend.setInvestNum(0); //已投人数
        lend.setPublishDate(LocalDateTime.now());

        //起息日期
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate lendStartDate = LocalDate.parse(borrowInfoApprovalVo.getLendStartDate(), dateTimeFormatter);
        lend.setLendStartDate(lendStartDate);
        //结束日期
        LocalDate lendEndDate = lendStartDate.plusMonths(borrowInfo.getPeriod());
        lend.setLendEndDate(lendEndDate);

        lend.setLendInfo(borrowInfoApprovalVo.getLendInfo());//标的描述

        //平台预期收益 = 标的金额 * (年化 / 12 * 期数)
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12), 8, BigDecimal.ROUND_DOWN);
        BigDecimal expectAmount = lend.getAmount().multiply(monthRate.multiply(new BigDecimal(lend.getPeriod())));
        lend.setExpectAmount(expectAmount);

        lend.setRealAmount(new BigDecimal(0));//实际收益
        lend.setStatus(LendStatusEnum.INVEST_RUN.getStatus());//标的状态
        lend.setCheckTime(LocalDateTime.now()); //审核时间
        lend.setCheckAdminId(1L); //审核人

        //存入数据库
        baseMapper.insert(lend);
    }

    @Override
    public List<Lend> selectList() {
        List<Lend> lendList = baseMapper.selectList(null);
        lendList.forEach(lend -> {
            String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", lend.getReturnMethod());
            String status = LendStatusEnum.getMsgByStatus(lend.getStatus());
            lend.getParam().put("returnMethod", returnMethod);
            lend.getParam().put("status", status);
        });
        return lendList;
    }

    @Override
    public Map<String, Object> getLendDetail(Long id) {

        //查询lend
        Lend lend = baseMapper.selectById(id);
        String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", lend.getReturnMethod());
        String status = LendStatusEnum.getMsgByStatus(lend.getStatus());
        lend.getParam().put("returnMethod", returnMethod);
        lend.getParam().put("status", status);


        //查询借款人对象：Borrower(BorrowerDetailVO)
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.eq("user_id", lend.getUserId());
        Borrower borrower = borrowerMapper.selectOne(borrowerQueryWrapper);
        BorrowerDetailVo borrowerDetailVO = borrowerService.getBorrowerDetailVOById(borrower.getId());

        //组装集合结果
        Map<String, Object> result = new HashMap<>();
        result.put("lend", lend);
        result.put("borrower", borrowerDetailVO);
        return result;
    }
}
