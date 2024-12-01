package com.gltqe.wladmin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.system.entity.po.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileInfoService extends IService<FileInfo> {
    /**
     * 文件上传接口
     *
     * @param path      存储路径
     * @param file      文件
     * @param sizeLimit 文件大小限制
     * @return com.gltqe.wladmin.entity.FileInfo
     * @author gltqe
     * @date 2022/7/3 0:44
     **/
    public FileInfo upload(String path, MultipartFile file, Long sizeLimit);
}
