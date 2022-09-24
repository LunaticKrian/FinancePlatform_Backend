package com.krian.finance.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krian.finance.core.pojo.entity.Borrower;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krian.finance.core.pojo.vo.BorrowerApprovalVo;
import com.krian.finance.core.pojo.vo.BorrowerDetailVo;
import com.krian.finance.core.pojo.vo.BorrowerVo;

/**
 * <p>
 * 借款人 服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
public interface BorrowerService extends IService<Borrower> {

    void saveBorrowerVoByUserId(BorrowerVo borrowerVo, Long userId);

    Integer getStatusByUserId(Long userId);

    IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword);

    BorrowerDetailVo getBorrowerDetailVOById(Long id);

    void approval(BorrowerApprovalVo borrowerApprovalVO);
}
