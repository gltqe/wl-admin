package com.gltqe.wladmin.project.test.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gltqe.wladmin.commons.base.BaseEntity;
import lombok.Data;

@Data
@TableName("test_info")
public class TestInfo extends BaseEntity {
    @TableField("content")
    private String content;

    @TableField("create_name")
    private String createName;

    @TableField("dept_name")
    private String deptName;

    @TableField("create_dept")
    private String createDept;
}
