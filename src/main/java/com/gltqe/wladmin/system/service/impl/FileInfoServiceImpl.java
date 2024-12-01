package com.gltqe.wladmin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.exception.FileException;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.commons.utils.FileUtil;
import com.gltqe.wladmin.commons.utils.StringUtil;
import com.gltqe.wladmin.system.entity.po.FileInfo;
import com.gltqe.wladmin.system.mapper.FileInfoMapper;
import com.gltqe.wladmin.system.service.FileInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {
    @Resource
    private FileInfoMapper fileInfoMapper;

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileInfo upload(String path, MultipartFile file, Long sizeLimit) {
        long size = file.getSize();
        if (sizeLimit != null && size > sizeLimit) {
            throw new FileException("超过文件大小限制!");
        }
        String originalFilename = file.getOriginalFilename();
        String suffixName = FileUtil.getSuffixName(originalFilename);
        String contentType = file.getContentType();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(originalFilename);
        fileInfo.setSuffix(suffixName);
        fileInfo.setContentType(contentType);
        fileInfo.setType(FileUtil.getTypeCode(suffixName));
        fileInfo.setByteSize(size);
        fileInfo.setKBSize(FileUtil.getFileSizeKB(size));
        fileInfo.setMBSize(FileUtil.getFileSizeMB(size));
        fileInfo.setPath(path);
        String uuidName = StringUtil.getUUId() + suffixName;
        fileInfo.setUuidName(uuidName);
        fileInfoMapper.insert(fileInfo);

        if (path.startsWith(".") || !Paths.get(path).isAbsolute()) {
            // 如果是相对路径，转换为绝对路径
            try {
                path = new File(path).getCanonicalPath();
            } catch (IOException e) {
                throw new WlException("存储路径异常，请联系管理员");
            }
        }

        try {
            File save = new File(path + File.separator + uuidName);
            File parentFile = save.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.transferTo(save);
        } catch (Exception e) {
            throw new WlException("文件上传异常!");
        }
        return fileInfo;
    }
}
