package com.gltqe.wladmin.framework.init;

import com.gltqe.wladmin.quartz.service.ScheduleJobService;
import com.gltqe.wladmin.system.service.SysConfigService;
import com.gltqe.wladmin.system.service.SysDictService;
import com.gltqe.wladmin.system.service.SysApiLimitService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author gltqe
 * @date 2023/5/19 10:04
 */
@Slf4j
@Component
public class InitRunner implements ApplicationRunner {
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private SysApiLimitService sysApiLimitService;
    @Resource
    private ScheduleJobService scheduleJobService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        sysConfigService.loadConfig();
        sysDictService.loadDict();
        sysApiLimitService.loadApiLimit();
        scheduleJobService.loadSchedule();
        log.info("初始化数据成功");
    }
}
