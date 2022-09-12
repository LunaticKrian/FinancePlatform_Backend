package com.krian.finance.core.service.impl;

import com.krian.finance.core.mapper.BorrowerMapper;
import com.krian.finance.core.service.BorrowerService;
import com.krian.finance.core.pojo.entity.Borrower;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
