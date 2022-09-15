package com.krian.finance.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krian.finance.core.pojo.dto.ExcelDictDto;
import com.krian.finance.core.pojo.entity.Dict;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
public interface DictMapper extends BaseMapper<Dict> {
    void insertBatch(List<ExcelDictDto> list);
}
