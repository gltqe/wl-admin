package com.gltqe.wladmin.project.test.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.framework.datascope.DataScope;
import com.gltqe.wladmin.project.test.entity.po.TestInfo;
import com.gltqe.wladmin.project.test.entity.dto.TestInfoDto;

public interface TestInfoService extends IService<TestInfo> {
    /**
     *
     *  mybatis-plus的方法只用实现 不用写sql
     *
     **/
    @Override
    @DataScope(ut = "test_info")
    default <E extends IPage<TestInfo>> E page(E page, Wrapper<TestInfo> queryWrapper) {
        return IService.super.page(page, queryWrapper);
    }

    IPage<TestInfo> pageMapper(TestInfoDto testInfoDto);

    IPage<TestInfo> pageCustom(TestInfoDto testInfoDto);

    void add(TestInfoDto testInfoDto);

}
