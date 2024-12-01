package com.gltqe.wladmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.framework.datascope.DataScope;
import com.gltqe.wladmin.system.entity.po.SysRole;
import com.gltqe.wladmin.system.entity.vo.SysRoleVo;
import com.gltqe.wladmin.system.entity.dto.SysRoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gltqe
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    SysRoleVo selectRoleMenuDept(String id);

    @DataScope(dt = "r")
    List<SysRole> getRoleByUser();

    @DataScope(dt = "r")
    Page<SysRoleVo> page(Page<SysRole> page, @Param("params") SysRoleDto sysRoleDto);

    List<SysRole> getUserRoleByUserId(String uid);

    List<String> getMenuIdsByRoleId(String rid);

    List<String> getDeptIdsByRoleId(String rid);

}
