/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gltqe.wladmin.quartz.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.quartz.entity.po.ScheduleJobLog;
import com.gltqe.wladmin.quartz.entity.dto.ScheduleJobLogDto;
import com.gltqe.wladmin.quartz.service.ScheduleJobLogService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/scheduleLog")
public class ScheduleJobLogController {
    @Resource
    private ScheduleJobLogService scheduleJobLogService;


    /**
     * 定时任务日志列表
     */
    @RequestMapping("/page")
    public Result page(@RequestBody ScheduleJobLogDto scheduleJobLogVo) {
        Page<ScheduleJobLog> page = scheduleJobLogVo.getPage();
        LambdaQueryWrapper<ScheduleJobLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(scheduleJobLogVo.getTaskName()),ScheduleJobLog::getTaskName,scheduleJobLogVo.getTaskName())
                .le(StringUtils.isNotBlank(scheduleJobLogVo.getEndDateTime()),ScheduleJobLog::getCreateTime,scheduleJobLogVo.getEndDateTime())
                .gt(StringUtils.isNotBlank(scheduleJobLogVo.getStartDateTime()),ScheduleJobLog::getCreateTime,scheduleJobLogVo.getStartDateTime())
                .orderByDesc(ScheduleJobLog::getCreateTime);
        IPage<ScheduleJobLog> r = scheduleJobLogService.page(page,wrapper);
        return Result.ok(r);
    }

    /**
     * 定时任务日志信息
     */
    @RequestMapping("/getOne")
    public Result getOne(@RequestBody ScheduleJobLogDto scheduleJobLogVo) {
        ScheduleJobLog log = scheduleJobLogService.getById(scheduleJobLogVo.getId());
        return Result.ok(log);
    }


}
