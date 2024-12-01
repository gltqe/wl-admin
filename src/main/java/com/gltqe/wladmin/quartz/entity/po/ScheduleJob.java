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

package com.gltqe.wladmin.quartz.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gltqe.wladmin.commons.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("schedule_job")
public class ScheduleJob extends BaseEntity {

    /**
     * 任务id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 任务名
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 任务组
     */
    @TableField("task_group")
    private String taskGroup;

    /**
     * 类名称
     */
    @TableField("bean_name")
    private String beanName;

    /**
     * 方法名
     */
    @TableField("method_name")
    private String methodName;

    /**
     * 参数
     */
    @TableField("params")
    private String params;

    /**
     * cron表达式
     */
    @TableField("cron_expression")
    private String cronExpression;

    /**
     * 错过后执行策略 0 不做处理 1 忽略本次 2 立即执行
     */
    @TableField("execute_policy")
    private String executePolicy;

    /**
     * 是否允许并发 0 允许 1 禁止
     */
    @TableField("concurrent")
    private String concurrent;

    /**
     * 任务状态
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;


}
