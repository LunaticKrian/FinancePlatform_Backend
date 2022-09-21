package com.krian.finance.core.controller.api;


import com.krian.common.result.R;
import com.krian.finance.core.pojo.entity.Dict;
import com.krian.finance.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "数据字典")
@Slf4j
@RestController
@RequestMapping("/api/core/dict")
public class UserDictController {

    @Autowired
    private DictService dictService;

    @ApiOperation("根据dictCode获取下级结点")
    @GetMapping("/findByDictCode/{dictCode}")
    public R findByDictCode(
            @ApiParam(value = "节点编码", required = true)
            @PathVariable String dictCode){

        List<Dict> list = dictService.findByDictCode(dictCode);
        return R.SUCCESS().data("dictList", list);
    }
}
