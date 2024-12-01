package com.gltqe.wladmin.system.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gltqe
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_position")
public class SysUserPosition extends Model<SysUserPosition> {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField("uid")
    private String uid;


    @TableField("pid")
    private String pid;
}
