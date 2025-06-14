package com.gltqe.wladmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gltqe.wladmin.system.entity.po.SysDept;
import com.gltqe.wladmin.system.entity.vo.SysDeptVo;
import com.gltqe.wladmin.system.entity.dto.SysDeptDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {
//    List<SysDept> getDeptByUser(SysDept sysDept,String sql);

    List<SysDeptVo>  getDeptByUser(@Param("params") SysDeptDto sysDeptDto);

    List<String> getDeptChildrenIds(String deptId);

    List<SysDept> getDeptChildren(String deptId);
}
