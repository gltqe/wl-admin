package com.gltqe.wladmin.project.test.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.commons.enums.OperationLogTypeEnum;
import com.gltqe.wladmin.framework.log.Log;
import com.gltqe.wladmin.project.test.entity.po.TestInfo;
import com.gltqe.wladmin.project.test.entity.dto.TestInfoDto;
import com.gltqe.wladmin.project.test.service.TestInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 测试数据权限
 *
 * @author gltqe
 * @date 2022/7/5 9:23
 **/

@RestController
@RequestMapping("/testInfo")
public class TestInfoController {
    @Resource
    private TestInfoService testInfoService;

    /**
     * 分页查询（使用 mybatis - plus service 中的方法）
     *
     * @param testInfoDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 2:16
     **/
    @RequestMapping("/pageService")
    @Log(name = "测试-分页查询-pageService", type = OperationLogTypeEnum.QUERY, recordParams = true, recordResult = true)
    public Result pageService(@RequestBody TestInfoDto testInfoDto) {
        LambdaQueryWrapper<TestInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(testInfoDto.getContent()), TestInfo::getContent, testInfoDto.getContent());
        Page<TestInfo> page = testInfoDto.getPage();
        IPage<TestInfo> iPage = testInfoService.page(page, wrapper);
        return Result.ok(iPage);
    }

    /**
     * 分页查询（使用 mybatis - plus mapper 中的方法)
     *
     * @param testInfoDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 2:17
     **/
    @RequestMapping("/pageMapper")
    @Log(name = "测试-分页查询-pageMapper", type = OperationLogTypeEnum.QUERY, recordParams = true, recordResult = true)
    public Result pageMapper(@RequestBody TestInfoDto testInfoDto) {
        IPage<TestInfo> iPage = testInfoService.pageMapper(testInfoDto);
        return Result.ok(iPage);
    }

    /**
     * 分页查询（使用自己写的sql）
     *
     * @param testInfoDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 2:17
     **/
    @RequestMapping("/pageCustom")
    @Log(name = "测试-分页查询-pageCustom", type = OperationLogTypeEnum.QUERY, recordParams = true, recordResult = true)
    public Result pageCustom(@RequestBody TestInfoDto testInfoDto) {
        IPage<TestInfo> iPage = testInfoService.pageCustom(testInfoDto);
        return Result.ok(iPage);
    }

    /**
     * 新增
     *
     * @param testInfoDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 2:17
     **/
    @RequestMapping("/add")
    @Log(name = "测试-新增", type = OperationLogTypeEnum.ADD, recordParams = true, recordResult = true)
    public Result add(@RequestBody TestInfoDto testInfoDto) {
        testInfoService.add(testInfoDto);
        return Result.ok();
    }

    @RequestMapping("/update")
    @Log(name = "测试-修改", type = OperationLogTypeEnum.UPDATE, recordParams = true, recordResult = true)
    public Result update(@RequestBody TestInfoDto testInfoDto) {
        TestInfo testInfo = BeanUtil.copyProperties(testInfoDto, TestInfo.class);
        testInfoService.updateById(testInfo);
        return Result.ok();
    }

    @RequestMapping("/remove")
    @Log(name = "测试-删除", type = OperationLogTypeEnum.REMOVE, recordParams = true, recordResult = true)
    public Result remove(@RequestBody List<String> ids) {
        testInfoService.removeByIds(ids);
        return Result.ok();
    }
}
