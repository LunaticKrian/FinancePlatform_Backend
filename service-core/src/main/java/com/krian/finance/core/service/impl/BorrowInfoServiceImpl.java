package com.krian.finance.core.service.impl;

import com.krian.finance.core.mapper.BorrowInfoMapper;
import com.krian.finance.core.service.BorrowInfoService;
import com.krian.finance.core.pojo.entity.BorrowInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

}
