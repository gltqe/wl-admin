package com.gltqe.wladmin.system.listener;

import com.gltqe.wladmin.framework.log.OperationLogEvent;
import com.gltqe.wladmin.monitor.entity.po.LogOperation;
import com.gltqe.wladmin.monitor.service.OperationLogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * @author gltqe
 * @date 2024/11/1 16:34
 */
@Slf4j
@Configuration
public class SystemListener {

    @Resource
    private OperationLogService operationLogService;

    @Async("CommonTaskExecutor")
    @EventListener
    public void handleLog(OperationLogEvent logEvent) {
        LogOperation operationLog = new LogOperation();
        BeanUtils.copyProperties(logEvent,operationLog);
        System.out.println(Thread.currentThread().getName());
        operationLogService.save(operationLog);
    }
}
