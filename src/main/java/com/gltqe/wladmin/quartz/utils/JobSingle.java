package com.gltqe.wladmin.quartz.utils;

import com.gltqe.wladmin.quartz.entity.po.ScheduleJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * @author gltqe
 * @date 2023/6/1 14:57
 */
@DisallowConcurrentExecution
public class JobSingle extends JobAbstract{
    @Override
    protected void doExecute(JobExecutionContext context, ScheduleJob job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}
