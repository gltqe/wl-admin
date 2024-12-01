package com.gltqe.wladmin.project.test.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.framework.datascope.DataScope;
import com.gltqe.wladmin.project.test.entity.po.TestInfo;
import com.gltqe.wladmin.project.test.entity.dto.TestInfoDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestInfoMapper extends BaseMapper<TestInfo> {
    /**
     *
     *  mybatis-plus的方法只用实现 不用写sql
     *
     **/
    @Override
    @DataScope(dt = "test_info",ut = "test_info")
    default <P extends IPage<TestInfo>> P selectPage(P page, Wrapper<TestInfo> queryWrapper) {
        return BaseMapper.super.selectPage(page, queryWrapper);
    }

    @DataScope(dt = "ti",ut = "ti")
    IPage<TestInfo> pageCustom(Page page, TestInfoDto testInfoVo);
}
