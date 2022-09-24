package com.krian.finance.core.service;

import com.krian.finance.core.pojo.entity.BorrowerAttach;
import com.baomidou.mybatisplus.extension.service.IService;
import com.krian.finance.core.pojo.vo.BorrowerAttachVo;

import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
public interface BorrowerAttachService extends IService<BorrowerAttach> {

    List<BorrowerAttachVo> selectBorrowerAttachVOList(Long id);
}
