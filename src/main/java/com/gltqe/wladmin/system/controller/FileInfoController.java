package com.gltqe.wladmin.system.controller;


import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.system.entity.po.FileInfo;
import com.gltqe.wladmin.system.entity.dto.FileInfoDto;
import com.gltqe.wladmin.system.service.FileInfoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 文件信息
 *
 * @author gltqe
 * @date 2022/7/3 0:43
 **/
@RestController
@RequestMapping("/fileInfo")
public class FileInfoController {
    @Resource
    private FileInfoService fileInfoService;

    /**
     * 根据ids获取文件信息列表
     *
     * @param ids
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 0:43
     **/
    @RequestMapping("/list")
    public Result list(@RequestBody List<String> ids) {
        List<FileInfo> list = fileInfoService.listByIds(ids);
        return Result.ok(list);
    }

    /**
     * 根据id获取文件信息
     *
     * @param fileInfoDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 0:43
     **/
    @RequestMapping("/getOne")
    public Result list(@RequestBody FileInfoDto fileInfoDto) {
        FileInfo info = fileInfoService.getById(fileInfoDto.getId());
        return Result.ok(info);
    }
}
