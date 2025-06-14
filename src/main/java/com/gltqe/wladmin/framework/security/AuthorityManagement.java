package com.gltqe.wladmin.framework.security;

import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.commons.exception.PermissionException;
import com.gltqe.wladmin.system.entity.bo.UserDetailsBo;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 权限管理
 * 用于spring security 注解PreAuthorize
 * 通过spel表达式 自定义控制权限
 *
 * @author gltqe
 * @date 2022/7/3 1:02
 **/
@Service("am")
public class AuthorityManagement {
    public boolean hasPermission(String permission) {
        UserDetailsBo userDetails = JwtUtil.getUserDetails();
        List<String> permissionList = userDetails.getPermissionList();
        if (permissionList.contains(Constant.ADMIN_PERMISSION)) {
            return true;
        }
        boolean contains = permissionList.contains(permission);
        if (!contains) {
            throw new PermissionException("无访问权限");
        }
        return true;
    }
}
