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
import com.gltqe.wladmin.quartz.entity.po.ScheduleJobLog;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleJobLogDto extends BaseDto<ScheduleJobLog> {

    /**
     * 日志id
     */
    private String id;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 任务分组
     */
    private String taskGroup;

    /**
     * spring bean名称
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
     * 执行状态    0：成功    1：失败
     */
    private String resultStatus;


//    /**
//     * 开始时间
//     */
//    private Date startDateTime;
//
//    /**
//     * 结束时间
//     */
//    private Date endDateTime;

    /**
     * 耗时(单位：毫秒)
     */
    private Long consumeTime;

    /**
     * 错误日志
     */
    private String errorMsg;

}
