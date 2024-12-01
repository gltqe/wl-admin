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

package com.gltqe.wladmin.quartz.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.exception.QuartzException;
import com.gltqe.wladmin.quartz.dao.ScheduleJobDao;
import com.gltqe.wladmin.quartz.entity.po.ScheduleJob;
import com.gltqe.wladmin.quartz.service.ScheduleJobService;
import com.gltqe.wladmin.quartz.utils.ScheduleUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobDao, ScheduleJob> implements ScheduleJobService {
    @Resource
    private Scheduler scheduler;
    @Resource
    private ScheduleJobDao scheduleJobDao;

    /**
     * 项目启动时，初始化定时器
     */
    public void loadSchedule() throws SchedulerException {
        scheduler.clear();
        List<ScheduleJob> scheduleJobList = list();
        for (ScheduleJob scheduleJob : scheduleJobList) {
            ScheduleUtils.createJob(scheduler, scheduleJob);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ScheduleJob scheduleJob) {
        String cronExpression = scheduleJob.getCronExpression();
        if (!CronExpression.isValidExpression(cronExpression)) {
            throw new QuartzException("cron表达式错误");
        }
        if (StringUtils.isBlank(scheduleJob.getConcurrent())){
            scheduleJob.setConcurrent(Constant.N);
        }
        if (StringUtils.isBlank(scheduleJob.getStatus())){
            scheduleJob.setStatus(Constant.N);
        }
        scheduleJob.setCreateTime(new Date());
//        scheduleJob.setStatus(Constant.N);
        scheduleJobDao.insert(scheduleJob);
        ScheduleUtils.createJob(scheduler, scheduleJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScheduleJob scheduleJob) {
        int i = scheduleJobDao.updateById(scheduleJob);
        if (i > 0) {
            ScheduleUtils.deleteJob(scheduler, scheduleJob);
        }
        ScheduleUtils.createJob(scheduler, scheduleJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(List<String> ids) {
        List<ScheduleJob> scheduleJobs = scheduleJobDao.selectBatchIds(ids);
        for (ScheduleJob scheduleJob : scheduleJobs) {
            ScheduleUtils.deleteJob(scheduler, scheduleJob);
        }
        scheduleJobDao.deleteBatchIds(ids);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(List<String> ids) {
        for (String id : ids) {
            ScheduleJob scheduleJob = scheduleJobDao.selectById(id);
            ScheduleUtils.run(scheduler, scheduleJob);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(List<String> ids) {
        List<ScheduleJob> scheduleJobs = scheduleJobDao.selectBatchIds(ids);
        for (ScheduleJob scheduleJob : scheduleJobs) {
            ScheduleUtils.pauseJob(scheduler, scheduleJob);
        }
        LambdaUpdateWrapper<ScheduleJob> scheduleUW = new LambdaUpdateWrapper<>();
        scheduleUW.set(ScheduleJob::getStatus, Constant.Y)
                .in(ScheduleJob::getId, ids);
        scheduleJobDao.update(null, scheduleUW);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(List<String> ids) {
        List<ScheduleJob> scheduleJobs = scheduleJobDao.selectBatchIds(ids);
        for (ScheduleJob scheduleJob : scheduleJobs) {
            ScheduleUtils.resumeJob(scheduler, scheduleJob);
        }
        LambdaUpdateWrapper<ScheduleJob> scheduleUW = new LambdaUpdateWrapper<>();
        scheduleUW.set(ScheduleJob::getStatus, Constant.N)
                .in(ScheduleJob::getId, ids);
        scheduleJobDao.update(null, scheduleUW);

    }

}
