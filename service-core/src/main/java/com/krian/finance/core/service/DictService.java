package com.krian.finance.core.service;

import com.krian.finance.core.pojo.dto.ExcelDictDto;
import com.krian.finance.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
public interface DictService extends IService<Dict> {
    void importData(InputStream inputStream);
}
