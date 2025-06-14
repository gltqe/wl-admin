package com.gltqe.wladmin.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.monitor.entity.po.LogOperation;
import com.gltqe.wladmin.monitor.entity.vo.OperationLogVo;
import com.gltqe.wladmin.monitor.entity.dto.OperationLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OperationLogMapper extends BaseMapper<LogOperation> {
    IPage<OperationLogVo> page(Page<LogOperation> page, @Param("operationLogVo") OperationLogDto operationLogVo);
}
