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
import com.gltqe.wladmin.quartz.entity.po.ScheduleJob;
import com.gltqe.wladmin.quartz.entity.dto.ScheduleJobDto;
import com.gltqe.wladmin.quartz.service.ScheduleJobService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/schedule")
public class ScheduleJobController {
    @Resource
    private ScheduleJobService scheduleJobService;

    /**
     * 定时任务列表
     */
    @RequestMapping("/page")
    public Result page(@RequestBody ScheduleJobDto scheduleJobDto) {
        LambdaQueryWrapper<ScheduleJob> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(scheduleJobDto.getTaskName()),ScheduleJob::getTaskName,scheduleJobDto.getTaskName())
                .eq(StringUtils.isNotBlank(scheduleJobDto.getTaskGroup()),ScheduleJob::getTaskGroup,scheduleJobDto.getTaskGroup())
                .eq(StringUtils.isNotBlank(scheduleJobDto.getStatus()),ScheduleJob::getStatus,scheduleJobDto.getStatus())
                .orderByDesc(ScheduleJob::getCreateTime);
        Page<ScheduleJob> page = scheduleJobDto.getPage();
        IPage<ScheduleJob> iPage = scheduleJobService.page(page,wrapper);
        return Result.ok(iPage);
    }

    /**
     * 定时任务信息
     */
    @RequestMapping("/getOne")
    public Result getOne(@RequestBody ScheduleJob scheduleJob) {
        ScheduleJob schedule = scheduleJobService.getById(scheduleJob);
        return Result.ok(schedule);
    }

    /**
     * 保存创建定时任务
     */
    @RequestMapping("/add")
    public Result add(@RequestBody ScheduleJob scheduleJob) {
        scheduleJobService.add(scheduleJob);
        return Result.ok();
    }

    /**
     * 修改定时任务
     */
    @RequestMapping("/update")
    public Result update(@RequestBody ScheduleJob scheduleJob) {
        scheduleJobService.update(scheduleJob);
        return Result.ok();
    }

    /**
     * 删除定时任务
     */
    @RequestMapping("/remove")
    public Result remove(@RequestBody List<String> ids) {
        scheduleJobService.remove(ids);
        return Result.ok();
    }


    /**
     * 立即执行多个任务
     */
    @RequestMapping("/run")
    public Result run(@RequestBody List<String> ids) {
        scheduleJobService.run(ids);
        return Result.ok();
    }

    /**
     * 暂停定时任务
     */
    @RequestMapping("/pause")
    public Result pause(@RequestBody List<String> ids) {
        scheduleJobService.pause(ids);
        return Result.ok();
    }

    /**
     * 恢复定时任务
     */
    @RequestMapping("/resume")
    public Result resume(@RequestBody List<String> ids) {
        scheduleJobService.resume(ids);
        return Result.ok();
    }

}
