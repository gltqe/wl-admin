package com.gltqe.wladmin.quartz.utils;

import com.gltqe.wladmin.quartz.entity.po.ScheduleJob;
import org.quartz.JobExecutionContext;

/**
 * @author
 * @date 2023/6/1 14:58
 */
public class JobThread extends JobAbstract {
    @Override
    protected void doExecute(JobExecutionContext context, ScheduleJob job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}
