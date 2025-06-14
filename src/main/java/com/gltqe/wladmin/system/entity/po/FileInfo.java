package com.gltqe.wladmin.system.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gltqe.wladmin.commons.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 文件信息实体类
 *
 * @author gltqe
 * @date 2022/7/3 0:44
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("file_info")
public class FileInfo extends BaseEntity {
    /**
     * 文件原名
     **/
    @TableField("name")
    private String name;

    /**
     * 存储路径
     **/
    @JsonIgnore
    @TableField("path")
    private String path;

    /**
     * 本地保存的UUID名称
     **/
    @TableField("uuid_name")
    private String uuidName;

    /**
     * 后缀名
     **/
    @TableField("suffix")
    private String suffix;

    /**
     * 内容类型
     **/
    @TableField("content_type")
    private String contentType;

    /**
     * 文件类型 0 图片 1 表格  2 文本 3 PDF 4 其他
     **/
    @TableField("type")
    private Integer type;

    /**
     * 文件大小(byte)
     **/
    @TableField("byte_size")
    private Long byteSize;

    /**
     * 文件大小(KB)
     **/
    @TableField("kb_size")
    private BigDecimal kBSize;

    /**
     * 文件大小(M)
     **/
    @TableField("mb_size")
    private BigDecimal mBSize;

}
