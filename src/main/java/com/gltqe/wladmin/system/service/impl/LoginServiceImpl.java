package com.gltqe.wladmin.system.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.gltqe.wladmin.commons.common.ConfigConstant;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.exception.TokenErrorException;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.commons.utils.IpUtil;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.monitor.entity.po.LogLogin;
import com.gltqe.wladmin.monitor.mapper.LoginLogMapper;
import com.gltqe.wladmin.system.entity.bo.UserDetailsBo;
import com.gltqe.wladmin.system.entity.dto.LoginDetailDto;
import com.gltqe.wladmin.system.entity.dto.LoginDto;
import com.gltqe.wladmin.system.entity.dto.RefreshTokenDto;
import com.gltqe.wladmin.system.entity.vo.CaptchaVo;
import com.gltqe.wladmin.system.entity.vo.LoginVo;
import com.gltqe.wladmin.system.service.AuthorityService;
import com.gltqe.wladmin.system.service.LoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private LoginLogMapper loginLogMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private AuthorityService authorityService;

    @Value("${token.access-ttl:3600}")
    private Long accessTtl;

    @Value("${token.refresh-ttl:360000}")
    private Long refreshTtl;

    @Value("${token.refresh-space:1800}")
    private Long refreshSpace;

    @Override
    public CaptchaVo getCaptcha() {
        // 验证码
        CaptchaVo captchaDto = new CaptchaVo();
        // 获取配置 判断是否需要验证码
        Object hasCaptchaObject = redisTemplate.opsForValue().get(ConfigConstant.HAS_CAPTCHA);
        if (ObjectUtil.isNotNull(hasCaptchaObject)) {
            Boolean hasCaptcha = Boolean.parseBoolean(String.valueOf(hasCaptchaObject));
            captchaDto.setHasCaptcha(hasCaptcha);
            if (!hasCaptcha) {
                return captchaDto;
            }
        }
        // 使用验证码
        captchaDto.setHasCaptcha(true);
        String captchaKey = IdUtil.fastSimpleUUID();
        captchaDto.setCaptchaKey(captchaKey);
        try {
//            GifCaptcha gifCaptcha = CaptchaUtil.createGifCaptcha(120, 40, 4);
            LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(130, 40, 4, 2);
            String text = lineCaptcha.getCode();
            String imageBase64Data = lineCaptcha.getImageBase64Data();
            captchaDto.setCaptchaValue(imageBase64Data);
            redisTemplate.opsForValue().set(Constant.CAPTCHA_KEY + captchaKey, text, 300L, TimeUnit.SECONDS);
            return captchaDto;
        } catch (Exception e) {
            log.error("获取验证码异常:", e);
            throw new WlException("获取验证码异常");
        }
    }

    /**
     * 登录接口
     *
     * @param loginVo
     * @param request
     * @return java.lang.String
     * @author gltqe
     * @date 2022/7/3 1:59
     **/
    @Override
    public LoginVo login(LoginDto loginVo, HttpServletRequest request) {
        // 验证码效验
        String captchaKey = loginVo.getCaptchaKey();
        String captchaValue = loginVo.getCaptchaValue();
        verCaptcha(captchaKey, captchaValue);
        //认证
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetailsBo principal = (UserDetailsBo) authenticate.getPrincipal();
        String principalUsername = principal.getUsername();
        //创建token
        String tokenId = IdUtil.fastSimpleUUID();
        Map<String, Object> map = new HashMap<>(2);
        map.put("username", principalUsername);
        map.put("tokenId", tokenId);
        map.put(Constant.TOKEN_TYPE_KEY, Constant.TOKEN_TYPE_ACCESS);
        String accessToken = JwtUtil.createToken(map, accessTtl);
        String refreshId = IdUtil.fastSimpleUUID();
        map.put("tokenId", refreshId);
        map.put(Constant.TOKEN_TYPE_KEY, Constant.TOKEN_TYPE_REFRESH);
        String refreshToken = JwtUtil.createToken(map, refreshTtl);

        //存入redis
        setLoginInfo(principal, request);
        redisTemplate.opsForValue().set(Constant.LOGIN_USER_KEY + principalUsername, principal, accessTtl, TimeUnit.SECONDS);
        // 登录日志
        recordLog(principal, request);
        // 响应结果
        LoginVo loginDto = new LoginVo();
        loginDto.setAccessToken(accessToken);
        loginDto.setRefreshToken(refreshToken);
        return loginDto;
    }

    public void verCaptcha(String captchaKey, String captchaValue) {
        // 获取配置 判断是否需要验证码
        Object hasCaptchaObject = redisTemplate.opsForValue().get(ConfigConstant.HAS_CAPTCHA);
        if (Objects.nonNull(hasCaptchaObject)) {
            Boolean hasCaptcha = Boolean.valueOf(String.valueOf(hasCaptchaObject));
            if (!hasCaptcha) {
                return;
            }
        }
        String key = Constant.CAPTCHA_KEY + captchaKey;
        Object o = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        if (Objects.nonNull(o)) {
            String s = String.valueOf(o);
            if (!s.equalsIgnoreCase(captchaValue)) {
                throw new WlException("验证码输入错误");
            }
        } else {
            throw new WlException("当前验证码已失效");
        }
    }

    public void recordLog(UserDetailsBo principal, HttpServletRequest request) {
        String username = principal.getUsername();
        String userId = principal.getUserId();
        String deptId = principal.getDeptId();
        String browser = IpUtil.getBrowser(request);
        String ipAddress = IpUtil.getIpAddress(request);
        String oS = IpUtil.getOs(request);
        LogLogin loginLog = new LogLogin();
        loginLog.setUid(userId);
        loginLog.setUsername(username);
        loginLog.setIp(ipAddress);
        loginLog.setBrowser(browser);
        loginLog.setOs(oS);
        loginLog.setTime(new Date());
        loginLog.setDeptId(deptId);
        loginLogMapper.insert(loginLog);
    }

    @Override
    public String refreshToken(RefreshTokenDto refreshTokenDto, HttpServletRequest request) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        boolean b = JwtUtil.verifyToken(refreshToken);
        if (b) {
            Boolean tokenExpired = JwtUtil.isTokenExpired(refreshToken);
            if (!tokenExpired) {
                Integer tokenType = JwtUtil.getTokenType(refreshToken);
                if (Constant.TOKEN_TYPE_REFRESH.equals(tokenType)) {
                    String username = JwtUtil.getUsernameByToken(refreshToken);
                    Boolean refresh = redisTemplate.opsForValue().setIfAbsent(Constant.TOKEN_REFRESH_SPACE_KEY + username, 1, refreshSpace, TimeUnit.SECONDS);
                    if (Boolean.TRUE.equals(refresh)) {
                        UserDetailsBo userDetail = (UserDetailsBo) userDetailsService.loadUserByUsername(username);
                        authorityService.setRolePermission(userDetail);
                        //创建token
                        Map<String, Object> map = new HashMap<>(2);
                        map.put("username", username);
                        map.put("tokenId", IdUtil.fastSimpleUUID());
                        map.put(Constant.TOKEN_TYPE_KEY, Constant.TOKEN_TYPE_ACCESS);
                        String accessToken = JwtUtil.createToken(map, accessTtl);
                        //存入redis
                        setLoginInfo(userDetail, request);
                        redisTemplate.opsForValue().set(Constant.LOGIN_USER_KEY + username, userDetail, accessTtl, TimeUnit.SECONDS);
                        return accessToken;
                    } else {
                        throw new TokenErrorException("刷新频率过高,请重新登录");
                    }
                } else {
                    throw new TokenErrorException("token错误,请重新登录");
                }
            } else {
                throw new TokenErrorException("refresh_token已过期,请重新登录");
            }
        } else {
            throw new TokenErrorException("token异常,请重新登录");
        }
    }

    public void setLoginInfo(UserDetailsBo sysUserDetails, HttpServletRequest request) {
        LoginDetailDto loginDetail = new LoginDetailDto();
        String browser = IpUtil.getBrowser(request);
        String ipAddress = IpUtil.getIpAddress(request);
        String oS = IpUtil.getOs(request);
        loginDetail.setBrowser(browser);
        loginDetail.setIp(ipAddress);
        loginDetail.setOs(oS);
        loginDetail.setLoginTime(new Date());
        sysUserDetails.setLoginDetail(loginDetail);
    }
}
