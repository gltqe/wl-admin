package com.gltqe.wladmin.framework.security;

import com.gltqe.wladmin.commons.common.ConfigConstant;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.enums.UserStatusEnum;
import com.gltqe.wladmin.commons.exception.LoginException;
import com.gltqe.wladmin.system.entity.bo.UserDetailsBo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.concurrent.TimeUnit;


/**
 * 认证方式
 *
 * @author gltqe
 * @date 2022/7/3 1:02
 **/
@Slf4j
@Configuration
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    @Resource
    private UserDetailServiceImpl userDetailService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("开始认证");
        //用户输入的用户名
        String usernameForm = authentication.getName();
        //用户输入的密码明文
        String passwordForm = authentication.getCredentials().toString();
        //查询到的数据库用户
        UserDetailsBo userDetail = (UserDetailsBo) userDetailService.loadUserByUsername(usernameForm);
        // 判断是否锁定
        checkLock(userDetail, usernameForm);
        // 校验密码
        checkPassword(userDetail, passwordForm);
        // 判断账号状态
        String status = userDetail.getStatus();
        if (!UserStatusEnum.NORMAL.getCode().equals(status)) {
            String desc = UserStatusEnum.getDesc(status);
            log.info("当前账号状态:" + desc);
            throw new LoginException("当前账号处于" + desc + "状态,请联系管理员!");
        }
        log.info("认证通过");
        //用户权限
        Collection<? extends GrantedAuthority> authorities = userDetail.getAuthorities();
        return new UsernamePasswordAuthenticationToken(userDetail, userDetail.getPassword(), authorities);
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }


    private void checkPassword(UserDetailsBo userDetail, String passwordForm) {
        //加盐
        String passwordFormSalt = passwordForm + userDetail.getSalt();
        //用户密码
        String password = userDetail.getPassword();
        String username = userDetail.getUsername();
        if (!passwordEncoder.matches(passwordFormSalt, password)) {
            // 锁定用户
            lockUser(username);
            throw new LoginException("用户名或密码错误!");
        } else {
            stringRedisTemplate.delete(Constant.ERROR_TIMES + username);
        }
    }

    private void checkLock(UserDetailsBo userDetail, String usernameForm) {
        String username = userDetail.getUsername();
        String cacheLock = stringRedisTemplate.opsForValue().get(Constant.LOGIN_LOCK + username);
        if (usernameForm.equals(cacheLock)) {
            throw new LoginException("当前用户[" + usernameForm + "]已被锁定,请稍后再试");
        }
    }

    private void lockUser(String username) {
        String errorTimesLimit = stringRedisTemplate.opsForValue().get(ConfigConstant.ERROR_TIMES_LIMIT);
        if (StringUtils.isNotBlank(errorTimesLimit) && Long.parseLong(errorTimesLimit) > 0) {
            // 增加错误次数
            Long increment = stringRedisTemplate.opsForValue().increment(Constant.ERROR_TIMES + username);
            long num = Long.parseLong(errorTimesLimit);
            if (num > 0 && increment >= num) {
                // 锁定用户时间
                String time = stringRedisTemplate.opsForValue().get(ConfigConstant.LOGIN_LOCK_TIME);
                if (StringUtils.isNotBlank(time)) {
                    long timeLong = Long.parseLong(time);
                    if (timeLong != 0) {
                        // 锁定用户
                        stringRedisTemplate.opsForValue().set(Constant.LOGIN_LOCK + username, username, timeLong, TimeUnit.MINUTES);
                        stringRedisTemplate.delete(Constant.ERROR_TIMES + username);
                    }
                }
            }
        }
    }
}
