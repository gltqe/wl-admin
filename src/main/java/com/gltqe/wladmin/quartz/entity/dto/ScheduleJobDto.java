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

package com.gltqe.wladmin.quartz.entity.dto;

import com.gltqe.wladmin.commons.base.BaseDto;
import com.gltqe.wladmin.quartz.entity.po.ScheduleJob;
import lombok.Data;


@Data
public class ScheduleJobDto extends BaseDto<ScheduleJob> {

    /**
     * 任务id
     */
    private String id;

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 任务组
     */
    private String taskGroup;

    /**
     * 类名称
     */
    private String beanName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 错过后执行策略 0 不做处理 1 忽略本次 2 立即执行
     */
    private String executePolicy;

    /**
     * 是否允许并发 0 允许 1 禁止
     */
    private String concurrent;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;


}
