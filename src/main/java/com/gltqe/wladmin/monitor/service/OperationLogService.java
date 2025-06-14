package com.gltqe.wladmin.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.monitor.entity.po.LogOperation;
import com.gltqe.wladmin.monitor.entity.vo.OperationLogVo;
import com.gltqe.wladmin.monitor.entity.dto.OperationLogDto;

import java.util.List;

public interface OperationLogService extends IService<LogOperation> {
    /**
     * 分页查询
     *
     * @param operationLogDto
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.gltqe.wladmin.monitor.entity.po.OperationLog>
     * @author gltqe
     * @date 2022/7/3 0:54
     **/
    IPage<OperationLogVo> page(OperationLogDto operationLogDto);

    /**
     * 获取操作日志详细信息
     *
     * @param id
     * @return com.gltqe.wladmin.monitor.entity.po.OperationLog
     * @author gltqe
     * @date 2022/7/3 0:54
     **/
    OperationLogVo getOperationLog(String id);

    /**
     * 删除操作日志
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 0:54
     **/
    void removeLog(List<String> ids);
}
