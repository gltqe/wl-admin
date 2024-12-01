package com.gltqe.wladmin.commons.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * 树形结构基类（所有需要构建树形结构继承此类）
 *
 * @author gltqe
 * @date 2022/7/2 23:43
 **/
@Data
public class TreeVo<T> extends BaseEntity {

    /**
     * 父id
     **/
    @TableField("parent_id")
    private String parentId;

    /**
     * 子菜单
     **/
    @TableField(exist = false)
    private List<T> children;

}
