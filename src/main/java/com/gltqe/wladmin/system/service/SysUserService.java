package com.gltqe.wladmin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.system.entity.po.SysUser;
import com.gltqe.wladmin.system.entity.vo.SysUserVo;
import com.gltqe.wladmin.system.entity.dto.PasswordDto;
import com.gltqe.wladmin.system.entity.dto.SysUserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;



/**
 * 用户
 *
 * @author gltqe
 * @date 2022/7/3 2:09
 **/
public interface SysUserService extends IService<SysUser> {

    /**
     * 获取当前登录用户信息
     *
     * @return: com.gltqe.wladmin.system.entity.dto.SysUserDto
     * @author gltqe
     * @date 2023/5/19 9:02
     **/
    SysUserVo getUserInfo();

    /**
     * 分页查询
     *
     * @param sysUserDto
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.gltqe.wladmin.system.entity.dto.SysUserDto>
     * @author gltqe
     * @date 2023/5/19 9:01
     **/
    IPage<SysUserVo> page(SysUserDto sysUserDto);

    /**
     * 修改状态
     *
     * @param sysUserDto
     * @author gltqe
     * @date 2023/5/19 9:01
     **/
    void updateStatus(SysUserDto sysUserDto);

    /**
     * 新增
     *
     * @param sysUserDto
     * @param avatarFile
     * @author gltqe
     * @date 2023/5/19 9:01
     **/
    void add(SysUserDto sysUserDto, MultipartFile avatarFile);

    /**
     * 修改
     *
     * @param sysUserDto
     * @author gltqe
     * @date 2023/5/19 9:00
     **/
    void updateUser(SysUserDto sysUserDto, MultipartFile avatarFile);

    /**
     * 获取详细信息
     * @param sysUserDto
     * @return: com.gltqe.wladmin.system.entity.dto.SysUserDto
     * @author gltqe
     * @date 2023/5/19 10:39
     **/
    SysUserVo getOneUser(SysUserDto sysUserDto);

    /**
     * 修改密码-个人信息页
     *
     * @param passwordDto
     * @author gltqe
     * @date 2023/5/19 8:58
     **/
    void updateSelfPassword(PasswordDto passwordDto);

    /**
     * 修改密码-用户列表页
     *
     * @param passwordDto
     * @author gltqe
     * @date 2023/5/19 8:58
     **/
    void updateOtherPassword(PasswordDto passwordDto);

    /**
     * 获取个人信息
     *
     * @return: com.gltqe.wladmin.system.entity.dto.SysUserDto
     * @author gltqe
     * @date 2023/5/19 8:58
     **/
    SysUserVo getSelfInfo();

    /**
     * 修改个人信息
     *
     * @param sysUserDto
     * @param avatarFile
     * @author gltqe
     * @date 2023/5/19 8:58
     **/
    void updateSelf( SysUserDto sysUserDto,MultipartFile avatarFile);

    /**
     * 导出
     * @author gltqe
     * @date 2024/5/13 15:59
     * @param sysUserDto
     * @param response
     * return
     */
    void exportUser(SysUserDto sysUserDto, HttpServletResponse response);

}
