package com.gltqe.wladmin.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.commons.common.DictConstant;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.commons.utils.DictUtil;
import com.gltqe.wladmin.system.entity.dto.SysDictDto;
import com.gltqe.wladmin.system.entity.po.SysDict;
import com.gltqe.wladmin.system.entity.po.SysDictItem;
import com.gltqe.wladmin.system.service.SysDictService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 字典
 *
 * @author gltqe
 * @date 2022/7/3 1:27
 **/
@RestController
@RequestMapping("/sysDict")
public class SysDictController {
    @Resource
    private SysDictService sysDictService;

    /**
     * 获取字典项
     *
     * @param sysDictDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:27
     **/
    @RequestMapping("/getDict")
    public Result getDict(@RequestBody SysDictDto sysDictDto) {
        Map<String, Object> result = new HashMap<>();
        String code = sysDictDto.getCode();
        if (StringUtils.isBlank(code)) {
            return Result.error("缺少参数");
        }
        List<SysDictItem> list = DictUtil.getCache(code);
        Integer type = sysDictDto.getType();

        if (DictConstant.DICT_LIST.equals(type) || DictConstant.DICT_LIST_MAP.equals(type) || type == null) {
            result.put("list", list);
        }
        if (DictConstant.DICT_MAP.equals(type) || DictConstant.DICT_LIST_MAP.equals(type) || type == null) {
            Map<Object, Object> map = list.stream().collect(Collectors.toMap(SysDictItem::getValue, Function.identity(), (v1, v2) -> v1));
            result.put("map", map);

        }
        return Result.ok(result);
    }

    /**
     * 分页查询
     *
     * @param sysDictDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:27
     **/
    @RequestMapping("/page")
    @PreAuthorize("@am.hasPermission('dict:list:query')")
    public Result page(@RequestBody SysDictDto sysDictDto) {
        String name = sysDictDto.getName();
        String code = sysDictDto.getCode();
        String status = sysDictDto.getStatus();
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), SysDict::getName, name)
                .like(StringUtils.isNotBlank(code), SysDict::getCode, code)
                .eq(StringUtils.isNotBlank(status), SysDict::getStatus, status)
                .orderByDesc(SysDict::getCreateTime);
        Page<SysDict> sysDictPage = sysDictService.page(sysDictDto.getPage(), wrapper);
        return Result.ok(sysDictPage);
    }

    /**
     * 修改状态
     *
     * @param sysDictDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:27
     **/
    @RequestMapping("/updateStatus")
    @PreAuthorize("@am.hasPermission('dict:list:update')")
    public Result updateStatus(@RequestBody SysDictDto sysDictDto) {
        sysDictService.updateStatus(sysDictDto);
        return Result.ok();
    }

    /**
     * 新增
     *
     * @param sysDictDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:27
     **/
    @RequestMapping("/add")
    @PreAuthorize("@am.hasPermission('dict:list:add')")
    public Result add(@RequestBody SysDictDto sysDictDto) {
        sysDictService.addDict(sysDictDto);
        return Result.ok();
    }

    /**
     * 获取详细信息
     *
     * @param sysDictDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:27
     **/
    @RequestMapping("/getOne")
    @PreAuthorize("@am.hasPermission('dict:list:query')")
    public Result getOne(@RequestBody SysDictDto sysDictDto) {
        SysDict dict = sysDictService.getById(sysDictDto.getId());
        return Result.ok(dict);
    }

    /**
     * 修改
     *
     * @param sysDictDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    @RequestMapping("/update")
    @PreAuthorize("@am.hasPermission('dict:list:update')")
    public Result update(@RequestBody SysDictDto sysDictDto) {
        sysDictService.updateDict(sysDictDto);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param codes
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    @RequestMapping("/remove")
    @PreAuthorize("@am.hasPermission('dict:list:remove')")
    public Result remove(@RequestBody List<String> codes) {
        sysDictService.removeDict(codes);
        return Result.ok("删除成功");
    }

    /**
     * 刷新redis
     *
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:28
     **/
    @RequestMapping("/refresh")
    @PreAuthorize("@am.hasPermission('dict:list:refresh')")
    public Result refresh() {
        sysDictService.refreshDict();
        return Result.ok("删除成功");
    }

}
