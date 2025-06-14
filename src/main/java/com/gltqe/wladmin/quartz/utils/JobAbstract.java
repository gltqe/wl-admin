package com.gltqe.wladmin.quartz.utils;

import cn.hutool.core.bean.BeanUtil;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.utils.SpringContextUtil;
import com.gltqe.wladmin.quartz.dao.ScheduleJobLogDao;
import com.gltqe.wladmin.quartz.entity.po.ScheduleJob;
import com.gltqe.wladmin.quartz.entity.po.ScheduleJobLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @author gltqe
 * @date 2023/6/1 14:50
 */
@Slf4j
public abstract class JobAbstract implements Job {

    private static ThreadLocal<ScheduleJobLog> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        Object o = context.getMergedJobDataMap().get(Constant.JOB_PARAM_KEY);
        ScheduleJob job = BeanUtil.copyProperties(o, ScheduleJob.class);

        before(job);
        try {
            log.info("开始执行定时任务:" + job.getTaskName());
            doExecute(context, job);
            log.info("结束执行定时任务:" + job.getTaskName());
            after(context, job, null);
        } catch (Exception e) {
            log.info("执行定时任务异常");
            e.printStackTrace();
            after(context, job, e);
        }
    }

    protected void before(ScheduleJob job) {
        // 记录开始时间及参数
        Date date = new Date();
        ScheduleJobLog log = new ScheduleJobLog();
        log.setTaskId(job.getId());
        log.setTaskName(job.getTaskName());
        log.setBeanName(job.getBeanName());
        log.setMethodName(job.getMethodName());
        log.setTaskGroup(job.getTaskGroup());
        log.setParams(job.getParams());
        log.setStartDateTime(date);
        log.setCreateTime(date);
        log.setResultStatus(Constant.TASK_RESULT_STATUS_PROGRESS);

        threadLocal.set(log);
        // 写入数据库当中
        SpringContextUtil.getBean(ScheduleJobLogDao.class).insert(log);
    }

    protected void after(JobExecutionContext context, ScheduleJob job, Exception e) {
        ScheduleJobLog log = threadLocal.get();
        threadLocal.remove();
        log.setEndDateTime(new Date());
        long consumeTime = log.getEndDateTime().getTime() - log.getStartDateTime().getTime();
        log.setConsumeTime(consumeTime);
        if (e != null) {
            log.setResultStatus(Constant.TASK_RESULT_STATUS_FAIL);
            String errorMsg = StringUtils.substring(e.getMessage(), 0, 1000);
            log.setErrorMsg(errorMsg);
        } else {
            log.setResultStatus(Constant.TASK_RESULT_STATUS_SUCCESS);
        }
        // 写入数据库当中
        SpringContextUtil.getBean(ScheduleJobLogDao.class).updateById(log);
    }

    protected abstract void doExecute(JobExecutionContext context, ScheduleJob job) throws Exception;
}
