package com.gltqe.wladmin.project.test.entity.dto;

import com.gltqe.wladmin.commons.base.BaseDto;
import lombok.Data;

@Data
public class TestInfoDto extends BaseDto {

    private String content;

    private String createName;

    private String deptName;

    private String createDept;
}
