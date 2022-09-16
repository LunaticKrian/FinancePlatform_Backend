package com.krian.finance.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krian.finance.core.listener.ExcelDictDtoListener;
import com.krian.finance.core.mapper.DictMapper;
import com.krian.finance.core.pojo.dto.ExcelDictDto;
import com.krian.finance.core.pojo.entity.Dict;
import com.krian.finance.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisTemplate redisTemplate;

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
        // 1.首先查询Redis中是否存在数据列表：
        try {
            log.info("尝试从Redis中获取数据！");
            // 从Redis数据库中取出数据：
            List<Dict> dictList = (List<Dict>) redisTemplate.opsForValue().get("finance:core:dictList" + parentId);
            if (dictList != null){
                log.info("获取Redis中Dict数据成功！");

                // 2.如果Redis中存在，则直接返回数据列表：
                return dictList;
            }
        } catch (Exception e) {
            log.info("Redis 服务器异常：" + ExceptionUtils.getStackTrace(e));
        }

        // 3.如果Redis中不存在，则从MySQL数据库中查询：（数据同步，设置数据过期时间，缓存失效之后会重新向MySQL数据库发起请求）
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", parentId);
        List<Dict> dictList = baseMapper.selectList(dictQueryWrapper);

        // 填充hashChildren字段：
        dictList.forEach(dict -> {
            // 判断当前节点是否子节点，找到当前的dict下级有没有子节点：
            boolean hasChildren = this.hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        });

        try {
            log.info("将数据存入Redis中！");
            // 4.查询完成之后，把数据缓存到Redis中：
            redisTemplate.opsForValue().set("finance:core:dictList" + parentId, dictList, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.info("Redis 服务器异常：" + ExceptionUtils.getStackTrace(e));
        }

        // 5.返回查询到的数据结果：
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
        int count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return true;
        }
        return false;
    }
}