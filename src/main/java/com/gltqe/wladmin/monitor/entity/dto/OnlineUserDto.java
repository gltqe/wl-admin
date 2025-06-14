package com.gltqe.wladmin.monitor.entity.dto;

import com.gltqe.wladmin.commons.base.BaseDto;
import lombok.Data;

/**
 * @author gltqe
 * @date 2024/3/16 14:15
 */
@Data
public class OnlineUserDto extends BaseDto {

    private String userId;

    private String username;

    private String deptId;

    private String ip;

}
