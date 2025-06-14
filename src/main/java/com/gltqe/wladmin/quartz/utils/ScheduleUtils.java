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

package com.gltqe.wladmin.quartz.utils;


import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.exception.QuartzException;
import com.gltqe.wladmin.quartz.entity.po.ScheduleJob;
import org.quartz.*;

/**
 * 定时任务工具类
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.2.0 2016-11-28
 */
public class ScheduleUtils {


    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(ScheduleJob scheduleJob) {
        TriggerKey key = TriggerKey.triggerKey(scheduleJob.getId(), scheduleJob.getTaskGroup());
        return key;
    }

    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(ScheduleJob scheduleJob) {
        JobKey key = JobKey.jobKey(scheduleJob.getId(), scheduleJob.getTaskGroup());
        return key;
    }

    /**
     * 获取表达式触发器
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new QuartzException("获取定时任务CronTrigger出现异常", e);
        }

    }

    /**
     * 创建定时任务
     */
    public static void createJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            // 根据是否并发获取任务类
            Class<? extends Job> jobClass = getJobClass(scheduleJob.getConcurrent());
            // 获取 JobKey
            JobKey jobKey = getJobKey(scheduleJob);
            //构建job信息  //创建一个JobDetail实例， 将该实例与job class 绑定
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobKey)
                    .build();

            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
            // 错过策略
            scheduleBuilder = getMisfirePolicy(scheduleBuilder, scheduleJob.getExecutePolicy());

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(scheduleJob.getId(), scheduleJob.getTaskGroup())
                    .withSchedule(scheduleBuilder).build();

            //放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(Constant.JOB_PARAM_KEY, scheduleJob);

            // 判断是否存在
            if (scheduler.checkExists(jobKey)) {
                // 防止创建时存在数据问题 先移除，然后在执行创建操作
                scheduler.deleteJob(jobKey);
            }

            scheduler.scheduleJob(jobDetail, trigger);

//            scheduler.start();

            //暂停任务
            if (Constant.Y.equals(scheduleJob.getStatus())) {
                pauseJob(scheduler, scheduleJob);
            }
        } catch (SchedulerException e) {
            throw new QuartzException("创建定时任务失败", e);
        }
    }

    /**
     * 错过策略   0 不做处理  1 忽略本次  2 立即执行
     */
    private static CronScheduleBuilder getMisfirePolicy(CronScheduleBuilder scheduleBuilder, String executePolicy) {
        if (Constant.MISFIRE_DO_NOTHING.equals(executePolicy)) {
            scheduleBuilder.withMisfireHandlingInstructionDoNothing();
        } else if (Constant.MISFIRE_IGNORE_THIS.equals(executePolicy)) {
            scheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
        } else if (Constant.MISFIRE_FIRE_NOW.equals(executePolicy)) {
            scheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
        }
        return scheduleBuilder;
    }

    /**
     * 获取job类型 单线程 多线程
     */
    private static Class<? extends Job> getJobClass(String concurrent) {
        return Constant.N.equals(concurrent) ? JobThread.class : JobSingle.class;
    }

    /**
     * 立即执行任务
     */
    public static void run(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            //参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(Constant.JOB_PARAM_KEY, scheduleJob);
            scheduler.triggerJob(getJobKey(scheduleJob), dataMap);
        } catch (SchedulerException e) {
            throw new QuartzException("立即执行定时任务失败", e);
        }
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            scheduler.pauseJob(getJobKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new QuartzException("暂停定时任务失败", e);
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            scheduler.resumeJob(getJobKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new QuartzException("恢复定时任务失败", e);
        }
    }

    /**
     * 删除定时任务
     */
    public static void deleteJob(Scheduler scheduler, ScheduleJob scheduleJob) {
        try {
            scheduler.deleteJob(getJobKey(scheduleJob));
        } catch (SchedulerException e) {
            throw new QuartzException("删除定时任务失败", e);
        }
    }

}
