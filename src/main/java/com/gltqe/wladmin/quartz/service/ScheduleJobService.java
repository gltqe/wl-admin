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

package com.gltqe.wladmin.quartz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.quartz.entity.po.ScheduleJob;
import org.quartz.SchedulerException;

import java.util.List;


public interface ScheduleJobService extends IService<ScheduleJob> {
    /**
     * 初始化
     */
    void loadSchedule() throws SchedulerException;

    /**
     * 保存定时任务
     */
    void add(ScheduleJob scheduleJob);

    /**
     * 更新定时任务
     */
    void update(ScheduleJob scheduleJob);

    /**
     * 批量删除定时任务
     */
    void remove(List<String> ids);


    /**
     * 立即执行
     */
    void run(List<String> ids);


    /**
     * 暂停运行
     */
    void pause(List<String> ids);

    /**
     * 恢复运行
     */
    void resume(List<String> ids);


}
