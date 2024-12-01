package com.gltqe.wladmin.system.entity.dto;

import com.gltqe.wladmin.commons.base.BaseDto;
import com.gltqe.wladmin.system.entity.po.FileInfo;
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
public class FileInfoDto extends BaseDto<FileInfo> {
    /**
     * 文件原名
     **/
    private String name;

    /**
     * 存储路径
     **/
    private String path;

    /**
     * 本地保存的UUID名称
     **/
    private String uuidName;

    /**
     * 后缀名
     **/
    private String suffix;

    /**
     * 内容类型
     **/
    private String contentType;

    /**
     * 文件类型 0 图片 1 表格  2 文本 3 PDF 4 其他
     **/
    private Integer type;

    /**
     * 文件大小(byte)
     **/
    private Long byteSize;

    /**
     * 文件大小(KB)
     **/
    private BigDecimal kBSize;

    /**
     * 文件大小(M)
     **/
    private BigDecimal mBSize;

}
