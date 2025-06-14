package com.gltqe.wladmin.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.monitor.entity.po.LogLogin;
import com.gltqe.wladmin.monitor.entity.vo.LoginLogVo;
import com.gltqe.wladmin.monitor.entity.dto.LoginLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginLogMapper extends BaseMapper<LogLogin> {

    Page<LoginLogVo> page(Page<LogLogin> page, @Param("params") LoginLogDto loginLogDto);
}
