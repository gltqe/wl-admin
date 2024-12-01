package com.gltqe.wladmin.monitor.controller;

import cn.hutool.Hutool;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.monitor.entity.vo.OnlineUserVo;
import com.gltqe.wladmin.monitor.entity.dto.OnlineUserDto;
import com.gltqe.wladmin.monitor.service.OnlineUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gltqe
 * @date 2024/3/16 14:11
 */
@RestController
@RequestMapping("/onlineUser")
public class OnlineUserController {

    @Resource
    private OnlineUserService onlineUserService;

    @RequestMapping("/page")
    public Result page(@RequestBody OnlineUserDto onlineUserDto) {
        IPage<OnlineUserVo> page = onlineUserService.page(onlineUserDto);
        return Result.ok(page);
    }

    @RequestMapping("/exit")
    public Result exit(@RequestBody OnlineUserDto onlineUserDto) {
        onlineUserService.exit(onlineUserDto);

        return Result.ok();
    }
}
