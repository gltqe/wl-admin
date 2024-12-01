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
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_role_user")
public class SysRoleUser extends Model<SysRoleUser> {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    @TableField("rid")
    private String rid;
    @TableField("uid")
    private String uid;
}
