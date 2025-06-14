package com.gltqe.wladmin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.commons.utils.CheckUtil;
import com.gltqe.wladmin.system.entity.dto.PasswordDto;
import com.gltqe.wladmin.system.entity.dto.SysUserDto;
import com.gltqe.wladmin.system.entity.vo.SysUserVo;
import com.gltqe.wladmin.system.service.SysUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户
 *
 * @author gltqe
 * @date 2022/7/3 1:35
 **/
@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    /**
     * 获取当前登录用户信息
     *
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:36
     **/
    @RequestMapping("/getUserInfo")
    public Result getUserInfo() {
        SysUserVo sysUserDto = sysUserService.getUserInfo();
        return Result.ok(sysUserDto);
    }

    /**
     * 分页查询
     *
     * @param sysUserDto
     * @return: com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2023/5/18 15:04
     **/
    @RequestMapping("/page")
    @PreAuthorize("@am.hasPermission('user:list:query')")
    public Result page(@RequestBody SysUserDto sysUserDto) {
        IPage<SysUserVo> iPage = sysUserService.page(sysUserDto);
        return Result.ok(iPage);
    }

    /**
     * 修改状态
     *
     * @param sysUserDto
     * @return: com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2023/5/19 8:56
     **/
    @RequestMapping("/updateStatus")
    @PreAuthorize("@am.hasPermission('user:list:update')")
    public Result updateStatus(@RequestBody SysUserDto sysUserDto) {
        sysUserService.updateStatus(sysUserDto);
        return Result.ok();
    }

    /**
     * 新增
     * @param sysUserDto
     * @param avatarFile
     * @return: com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2023/5/19 8:56
     **/
    @RequestMapping("/add")
    @PreAuthorize("@am.hasPermission('user:list:add')")
    public Result add(@ModelAttribute SysUserDto sysUserDto, @RequestPart(value = "avatarFile",required = false) MultipartFile avatarFile) {
        sysUserService.add(sysUserDto,avatarFile);
        return Result.ok();
    }

    /**
     * 获取详细信息
     *
     * @param sysUserDto
     * @return: com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2023/5/19 8:56
     **/
    @RequestMapping("/getOne")
    @PreAuthorize("@am.hasPermission('user:list:query')")
    public Result getOne(@RequestBody SysUserDto sysUserDto) {
        SysUserVo sysUserVo = sysUserService.getOneUser(sysUserDto);
        return Result.ok(sysUserVo);
    }

    /**
     * 修改
     * @param sysUserDto
     * @param avatarFile
     * @return: com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2023/5/19 8:57
     **/
    @RequestMapping("/update")
    @PreAuthorize("@am.hasPermission('user:list:update')")
    public Result update(@ModelAttribute SysUserDto sysUserDto, @RequestPart(value = "avatarFile",required = false) MultipartFile avatarFile) {
        sysUserService.updateUser(sysUserDto,avatarFile);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param ids
     * @return: com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2023/5/19 8:57
     **/
    @RequestMapping("/remove")
    @PreAuthorize("@am.hasPermission('user:list:remove')")
    public Result remove(@RequestBody List<String> ids) {
        sysUserService.removeByIds(ids);
        return Result.ok("删除成功");
    }

    /**
     * 修改密码-个人信息页
     *
     * @param passwordDto
     * @return: com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2023/5/19 8:57
     **/
    @RequestMapping("/updateSelfPassword")
    public Result updateSelfPassword(@RequestBody PasswordDto passwordDto) {
        String old = passwordDto.getOld();
        if (StringUtils.isBlank(old)) {
            return Result.error("请输入旧密码");
        }
        String new1 = passwordDto.getNew1();
        String new2 = passwordDto.getNew2();
        if (!CheckUtil.match(Constant.REGEXP_PASSWORD, new1)) {
            return Result.error("密码效验未通过");
        }

        if (!new1.equals(new2)) {
            return Result.error("两次输入不一致");
        }
        sysUserService.updateSelfPassword(passwordDto);
        return Result.ok("删除成功");
    }

    /**
     * 修改密码-用户列表页
     *
     * @param passwordDto
     * @return: com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2023/5/19 8:57
     **/
    @RequestMapping("/updateOtherPassword")
    @PreAuthorize("@am.hasPermission('user:list:updateOtherPassword')")
    public Result updateOtherPassword(@RequestBody PasswordDto passwordDto) {
        String new1 = passwordDto.getNew1();
        String new2 = passwordDto.getNew2();
        if (!CheckUtil.match(Constant.REGEXP_PASSWORD, new1)) {
            return Result.error("密码效验未通过");
        }

        if (!new1.equals(new2)) {
            return Result.error("两次输入不一致");
        }
        sysUserService.updateOtherPassword(passwordDto);
        return Result.ok("删除成功");
    }

    /**
     * 获取个人信息
     *
     * @return: com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2023/5/19 8:57
     **/
    @RequestMapping("/getSelfInfo")
    public Result getSelfInfo() {
        SysUserVo sysUserDto = sysUserService.getSelfInfo();
        return Result.ok(sysUserDto);
    }

    /**
     * 修改个人信息
     *
     * @param sysUserDto
     * @param avatarFile
     * @return: com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2023/5/19 8:57
     **/
    @RequestMapping("/updateSelf")
    public Result updateSelf(@ModelAttribute SysUserDto sysUserDto, @RequestPart(value = "avatar",required = false) MultipartFile avatarFile) {
//        SysUserDto sysUserVo = JSONObject.parseObject(user, SysUserDto.class);
        sysUserService.updateSelf(sysUserDto, avatarFile);
        return Result.ok();
    }

    /**
     * 导出
     * @author gltqe
     * @date 2024/5/13 18:03
     * @param sysUserDto
     * @param response
     * return
     */
    @RequestMapping("/exportUser")
    public void exportUser(@RequestBody SysUserDto sysUserDto, HttpServletResponse response){
        sysUserService.exportUser(sysUserDto,response);
    }

}
