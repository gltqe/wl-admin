package com.gltqe.wladmin.monitor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.monitor.entity.vo.LoginLogVo;
import com.gltqe.wladmin.monitor.entity.dto.LoginLogDto;
import com.gltqe.wladmin.monitor.service.LoginLogService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 登录日志
 *
 * @author gltqe
 * @date 2022/7/3 0:48
 **/
@RestController
@RequestMapping("/loginLog")
public class LoginLogController {

    @Resource
    private LoginLogService loginLogService;

    /**
     * 分页查询
     *
     * @param loginLogDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 0:48
     **/
    @RequestMapping("/page")
    @PreAuthorize("@am.hasPermission('login:list:query')")
    public Result getRolePageByUser(@RequestBody LoginLogDto loginLogDto) {
        IPage<LoginLogVo> iPage = loginLogService.page(loginLogDto);
        return Result.ok(iPage);
    }

    /**
     * 日志详细信息
     *
     * @param loginLogDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 0:48
     **/
    @RequestMapping("/getOne")
    @PreAuthorize("@am.hasPermission('login:list:query')")
    public Result getOne(@RequestBody LoginLogDto loginLogDto) {
        LoginLogVo login = loginLogService.getLogLogin(loginLogDto.getId());
        return Result.ok(login);
    }

    /**
     * 删除登录日志
     *
     * @param ids
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 0:53
     **/
    @RequestMapping("/remove")
    @PreAuthorize("@am.hasPermission('role:list:remove')")
    public Result remove(@RequestBody List<String> ids) {
        loginLogService.removeLog(ids);
        return Result.ok("删除成功");
    }
}
