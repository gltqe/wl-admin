package com.gltqe.wladmin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gltqe.wladmin.system.entity.po.SysMenu;
import com.gltqe.wladmin.system.entity.vo.SysMenuVo;
import com.gltqe.wladmin.system.entity.dto.SysMenuDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenuVo> getAllMenus(@Param("params")SysMenu sysMenu);

    List<SysMenuVo> getMenuPermission(String userId);

    List<SysMenuVo> getMenuByUser(@Param("userId") String userId, @Param("params") SysMenuDto sysMenuDto);


}
