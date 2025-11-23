package com.gltqe.wladmin.monitor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.commons.enums.OperationLogTypeEnum;
import com.gltqe.wladmin.framework.log.Log;
import com.gltqe.wladmin.monitor.entity.vo.OperationLogVo;
import com.gltqe.wladmin.monitor.entity.dto.OperationLogDto;
import com.gltqe.wladmin.monitor.service.OperationLogService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 操作记录
 *
 * @author gltqe
 * @date 2022/7/3 0:49
 **/
@RestController
@RequestMapping("/operationLog")
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    /**
     * 分页查询
     *
     * @param operationLogDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 0:49
     **/
    @RequestMapping("/page")
    @PreAuthorize("@am.hasPermission('operation:list:query')")
//    @Log(name = "操作日志-分页查询", type = OperationLogTypeEnum.QUERY, recordParams = true, recordResult = true)
    public Result getRolePageByUser(@RequestBody OperationLogDto operationLogDto) {
        IPage<OperationLogVo> iPage = operationLogService.page( operationLogDto);
        return Result.ok(iPage);
    }

    /**
     * 日志详细信息
     *
     * @param operationLogDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 0:49
     **/
    @RequestMapping("/getOne")
    @PreAuthorize("@am.hasPermission('operation:list:query')")
    public Result getOne(OperationLogDto operationLogDto) {
        OperationLogVo operation = operationLogService.getOperationLog(operationLogDto.getId());
        return Result.ok(operation);
    }

    /**
     * 删除日志
     *
     * @param ids
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 0:49
     **/
    @RequestMapping("/remove")
    @PreAuthorize("@am.hasPermission('operation:list:remove')")
    public Result remove(@RequestBody List<String> ids) {
        operationLogService.removeLog(ids);
        return Result.ok("删除成功");
    }
}
