package com.gltqe.wladmin.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gltqe.wladmin.monitor.entity.po.LogLogin;
import com.gltqe.wladmin.monitor.entity.vo.LoginLogVo;
import com.gltqe.wladmin.monitor.entity.dto.LoginLogDto;

import java.util.List;

public interface LoginLogService extends IService<LogLogin> {
    /**
     * 分页查询
     *
     * @param loginLogVo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.gltqe.wladmin.monitor.entity.LogLogin>
     * @author gltqe
     * @date 2022/7/3 0:53
     **/
    IPage<LoginLogVo> page(LoginLogDto loginLogVo);

    /**
     * 获取登录日志详细信息
     *
     * @param id
     * @return com.gltqe.wladmin.monitor.entity.LogLogin
     * @author gltqe
     * @date 2022/7/3 0:53
     **/
    LoginLogVo getLogLogin(String id);

    /**
     * 删除日志
     *
     * @param ids
     * @author gltqe
     * @date 2022/7/3 0:54
     **/
    void removeLog(List<String> ids);
}
