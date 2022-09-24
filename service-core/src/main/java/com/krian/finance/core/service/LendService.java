package com.krian.finance.core.service;

import com.krian.finance.core.pojo.entity.BorrowInfo;
import com.krian.finance.core.pojo.entity.Lend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krian.finance.core.pojo.vo.BorrowInfoApprovalVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
public interface LendService extends IService<Lend> {

    void createLend(BorrowInfoApprovalVo borrowInfoApprovalVo, BorrowInfo borrowInfo);

    List<Lend> selectList();

    Map<String, Object> getLendDetail(Long id);
}
