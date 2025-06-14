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
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


/**
 * @author gltqe
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("schedule_job_log")
public class ScheduleJobLog extends Model<ScheduleJobLog> {

    /**
     * 日志id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 任务id
     */
    @TableField("task_id")
    private String taskId;

    /**
     * 任务名
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 任务分组
     */
    @TableField("task_group")
    private String taskGroup;

    /**
     * spring bean名称
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
     * 执行状态    0：成功    1：失败
     */
    @TableField("result_status")
    private String resultStatus;


    /**
     * 开始时间
     */
    @TableField("start_date_time")
    private Date startDateTime;

    /**
     * 开始时间
     */
    @TableField("end_date_time")
    private Date endDateTime;

    /**
     * 耗时(单位：毫秒)
     */
    @TableField("consume_time")
    private Long consumeTime;

    /**
     * 错误日志
     */
    @TableField("error_msg")
    private String errorMsg;

    /**
     * 创建时间
     **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;
}
