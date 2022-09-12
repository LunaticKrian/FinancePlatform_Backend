package com.krian.finance.core.service.impl;

import com.krian.finance.core.mapper.LendItemMapper;
import com.krian.finance.core.service.LendItemService;
import com.krian.finance.core.pojo.entity.LendItem;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标的出借记录表 服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
@Service
public class LendItemServiceImpl extends ServiceImpl<LendItemMapper, LendItem> implements LendItemService {

}
