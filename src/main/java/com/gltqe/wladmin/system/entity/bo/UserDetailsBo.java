package com.gltqe.wladmin.system.entity.bo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.gltqe.wladmin.system.entity.dto.LoginDetailDto;
import com.gltqe.wladmin.system.entity.po.SysRole;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// 用于 UserDetailServiceImpl 中构建User
@Data
public class UserDetailsBo implements UserDetails {

    private String userId;
    private String username;
    private String password;
    private String salt;
    private String status;
    private String deptId;
    private List<String> permissionList;
    private List<SysRole> roleList;
    private LoginDetailDto loginDetail;

    public UserDetailsBo() {
    }

    public UserDetailsBo(String userId, String username, String password, String salt, String status, String deptId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.status = status;
        this.deptId = deptId;
    }

    public UserDetailsBo(String userId, String username, String password, String salt, String status, List<String> permissionList, List<SysRole> roleList) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.status = status;
        this.roleList = roleList;
        this.permissionList = permissionList;
    }


    @Override
    //@JSONField(serialize = false) //redis 保存时该字段不序列化
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return true;
    }
}
