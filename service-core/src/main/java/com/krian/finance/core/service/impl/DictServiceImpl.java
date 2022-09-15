package com.krian.finance.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.krian.finance.core.listener.ExcelDictDtoListener;
import com.krian.finance.core.mapper.DictMapper;
import com.krian.finance.core.pojo.dto.ExcelDictDto;
import com.krian.finance.core.service.DictService;
import com.krian.finance.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author krian
 * @since 2022-09-11
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDictDto.class, new ExcelDictDtoListener(baseMapper)).sheet().doRead();
        log.info("Excel导入成功");
    }

    @Override
    public List<ExcelDictDto> listDictData() {
        List<Dict> dictList = baseMapper.selectList(null);

        // 创建ExcelDictDto列表，转换Dict对象：
        ArrayList<ExcelDictDto> excelDictDtoArrayList = new ArrayList<>(dictList.size());
        dictList.forEach(dict -> {
            ExcelDictDto excelDictDto = new ExcelDictDto();
            BeanUtils.copyProperties(dict, excelDictDto);
            excelDictDtoArrayList.add(excelDictDto);
        });
        return excelDictDtoArrayList;
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", parentId);
        List<Dict> dictList = baseMapper.selectList(dictQueryWrapper);

        // 填充hashChildren字段：
        dictList.forEach(dict -> {
            // 判断当前节点是否子节点，找到当前的dict下级有没有子节点：
            boolean hasChildren = this.hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        });
        return dictList;
    }

    /**
     * 判断当前id所以的所在节点下是否有子节点
     *
     * @param id
     * @return boolean
     */
    private boolean hasChildren(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        Long count = baseMapper.selectCount(queryWrapper);
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }
}