package com.gltqe.wladmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.framework.datascope.DataScope;
import com.gltqe.wladmin.system.entity.dto.SysUserDto;
import com.gltqe.wladmin.system.entity.po.SysUser;
import com.gltqe.wladmin.system.entity.vo.SysUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    List<String> getPermissionByUser(String uid);

    SysUserVo getSelfInfo(String userId);

    @DataScope(dt = "d", df = "id")
    Page<SysUserVo> page(Page<SysUser> page, @Param("params") SysUser sysUser);

    @DataScope(dt = "d", df = "id")
    List<SysUserVo> getList(@Param("params") SysUserDto sysUserDto);

    List<SysUserVo> getListByIds(@Param("list") List<String> ids);

    @DataScope(dt = "su", df = "dept_id")
    List<SysUser> getListByUser();


}
