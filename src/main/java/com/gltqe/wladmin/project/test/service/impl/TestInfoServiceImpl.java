package com.gltqe.wladmin.project.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.project.test.entity.po.TestInfo;
import com.gltqe.wladmin.project.test.entity.dto.TestInfoDto;
import com.gltqe.wladmin.project.test.mapper.TestInfoMapper;
import com.gltqe.wladmin.project.test.service.TestInfoService;
import com.gltqe.wladmin.system.entity.po.SysDept;
import com.gltqe.wladmin.system.mapper.SysDeptMapper;
import com.gltqe.wladmin.system.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class TestInfoServiceImpl extends ServiceImpl<TestInfoMapper, TestInfo> implements TestInfoService {
    @Resource
    private TestInfoMapper testInfoMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public IPage<TestInfo> pageMapper(TestInfoDto testInfoDto) {
        LambdaQueryWrapper<TestInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(testInfoDto.getContent()), TestInfo::getContent,testInfoDto.getContent());
        Page<TestInfo> page = testInfoDto.getPage();
        IPage<TestInfo> iPage = testInfoMapper.selectPage(page, wrapper);
        return iPage;
    }

    @Override
    public IPage<TestInfo> pageCustom(TestInfoDto testInfoDto) {
        Page page = testInfoDto.getPage();
        IPage<TestInfo> iPage = testInfoMapper.pageCustom(page,testInfoDto);
        return iPage;
    }

    @Override
    public void add(TestInfoDto testInfoDto) {
        TestInfo testInfo = new TestInfo();
        testInfo.setCreateName(JwtUtil.getUsername());
        testInfo.setContent(testInfoDto.getContent());
        String deptId = JwtUtil.getDeptId();
        testInfo.setCreateDept(deptId);
        SysDept sysDept = sysDeptMapper.selectById(deptId);
        if (sysDept!=null){
            testInfo.setDeptName(sysDept.getName());
        }
        testInfoMapper.insert(testInfo);
    }
}
