package com.gltqe.wladmin.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gltqe.wladmin.monitor.entity.vo.OnlineUserVo;
import com.gltqe.wladmin.monitor.entity.dto.OnlineUserDto;

/**
 * @author gltqe
 * @date 2024/3/18 13:03
 */
public interface OnlineUserService {
    IPage<OnlineUserVo> page(OnlineUserDto onlineUserVo);

    void exit(OnlineUserDto onlineUserVo);


}
