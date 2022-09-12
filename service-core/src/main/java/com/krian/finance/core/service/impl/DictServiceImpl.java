package com.krian.finance.core.service.impl;

import com.krian.finance.core.mapper.DictMapper;
import com.krian.finance.core.service.DictService;
import com.krian.finance.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

}
