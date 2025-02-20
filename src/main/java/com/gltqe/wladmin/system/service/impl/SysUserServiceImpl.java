package com.gltqe.wladmin.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.HexUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.enums.UserStatusEnum;
import com.gltqe.wladmin.commons.exception.LoginException;
import com.gltqe.wladmin.commons.exception.WlException;
import com.gltqe.wladmin.commons.utils.EncryptUtil;
import com.gltqe.wladmin.commons.utils.ExcelUtil;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.system.entity.bo.UserDetailsBo;
import com.gltqe.wladmin.system.entity.dto.PasswordDto;
import com.gltqe.wladmin.system.entity.dto.SysUserDto;
import com.gltqe.wladmin.system.entity.po.*;
import com.gltqe.wladmin.system.entity.vo.SysUserVo;
import com.gltqe.wladmin.system.mapper.SysRoleMapper;
import com.gltqe.wladmin.system.mapper.SysRoleUserMapper;
import com.gltqe.wladmin.system.mapper.SysUserMapper;
import com.gltqe.wladmin.system.mapper.SysUserPositionMapper;
import com.gltqe.wladmin.system.service.FileInfoService;
import com.gltqe.wladmin.system.service.SysRoleUserService;
import com.gltqe.wladmin.system.service.SysUserPositionService;
import com.gltqe.wladmin.system.service.SysUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户
 *
 * @author gltqe
 * @date 2022/7/3 2:09
 **/
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleUserService sysRoleUserService;
    @Resource
    private FileInfoService fileInfoService;
    @Resource
    private SysUserPositionService sysUserPositionService;
    @Resource
    private SysUserPositionMapper sysUserPositionMapper;

    @Value("${upload.path.avatar}")
    private String avatarPath;

    /**
     * 获取当前登录用户信息
     *
     * @return: com.gltqe.wladmin.system.entity.po.SysUser
     * @author gltqe
     * @date 2023/5/19 9:02
     **/
    @Override
    public SysUserVo getUserInfo() {
        UserDetailsBo userDetails = JwtUtil.getUserDetails();
        String username = userDetails.getUsername();
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username).eq(SysUser::getStatus, UserStatusEnum.NORMAL.getCode());
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        if (Objects.nonNull(sysUser)) {
            String avatar = sysUser.getAvatar();
            FileInfo fileInfo = fileInfoService.getById(avatar);
            refreshRedis(userDetails, sysUser);
            SysUserVo sysUserVo = BeanUtil.copyProperties(sysUser, SysUserVo.class);
            if (fileInfo != null) {
                sysUserVo.setAvatarName(fileInfo.getUuidName());
            }
            return sysUserVo;
        } else {
            throw new LoginException("未查询到当前用户信息,请联系管理员");
        }
    }

    private void refreshRedis(UserDetailsBo userDetail, SysUser sysUser) {
        String username = userDetail.getUsername();
        Long expire = redisTemplate.getExpire(Constant.LOGIN_USER_KEY + username);
        // 刷新部门id 用户状态
        userDetail.setDeptId(sysUser.getDeptId());
        userDetail.setStatus(sysUser.getStatus());
        // 查询当前用户已分配角色
        String uid = sysUser.getId();
        List<SysRole> roleList = sysRoleMapper.getUserRoleByUserId(uid);
        userDetail.setRoleList(roleList);
        // 查询权限
        List<String> permissionList = new ArrayList<>();
        if (JwtUtil.isAdmin(username)) {
            permissionList.add(Constant.ADMIN_PERMISSION);
        } else {
            if (roleList.isEmpty()) {
                throw new LoginException("用户状态异常,未分配任何角色,请联系管理员");
            }
            permissionList = sysUserMapper.getPermissionByUser(uid);
        }
        userDetail.setPermissionList(permissionList);
//        userDetail.setIsLogin(false);
        redisTemplate.opsForValue().set(Constant.LOGIN_USER_KEY + username, userDetail, Long.valueOf(expire), TimeUnit.SECONDS);
    }

    /**
     * 分页查询
     *
     * @param sysUserDto 入参
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.gltqe.wladmin.system.entity.dto.SysUserDto>
     * @author gltqe
     * @date 2023/5/19 9:02
     **/
    @Override
    public IPage<SysUserVo> page(SysUserDto sysUserDto) {
        sysUserDto.setId(JwtUtil.getUserId());
        Page<SysUser> page = sysUserDto.getPage();
        SysUser sysUser = BeanUtil.copyProperties(sysUserDto, SysUser.class);
        Page<SysUserVo> userPage = sysUserMapper.page(page, sysUser);
        List<SysUserVo> records = userPage.getRecords();
        for (SysUserVo record : records) {
            String username = record.getUsername();
            Object o = redisTemplate.opsForValue().get(Constant.LOGIN_LOCK + username);
            if (Objects.nonNull(o)) {
                record.setStatus(UserStatusEnum.LOCK.getCode());
            }
        }
        return userPage;
    }

    /**
     * 修改状态
     *
     * @param sysUserDto 入参
     * @author gltqe
     * @date 2023/5/19 9:02
     **/
    @Override
    public void updateStatus(SysUserDto sysUserDto) {
        String id = sysUserDto.getId();
        String status = sysUserDto.getStatus();
        if (!Constant.N.equals(status)) {
            status = Constant.Y;
        }
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(SysUser::getStatus, status).eq(SysUser::getId, id);
        sysUserMapper.update(null, wrapper);
        SysUser sysUser = sysUserMapper.selectById(id);
        String username = sysUser.getUsername();
        redisTemplate.delete(Constant.LOGIN_USER_KEY + username);
    }

    /**
     * 新增
     *
     * @param sysUserDto 入参
     * @author gltqe
     * @date 2023/5/19 9:02
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserDto sysUserDto, MultipartFile avatarFile) {
        String password = sysUserDto.getPassword();
        String salt = HexUtil.encodeHexStr(EncryptUtil.getRandomByte(16));
        String encode = passwordEncoder.encode(password + salt);
        sysUserDto.setPassword(encode);
        String avatar = null;
        if (avatarFile != null) {
            FileInfo fileInfo = fileInfoService.upload(avatarPath, avatarFile, Constant.AVATAR_SIZE);
            avatar = fileInfo.getId();
        }
        SysUser sysUser = BeanUtil.copyProperties(sysUserDto, SysUser.class);
        sysUser.setAvatar(avatar);
        checkUsername(sysUser.getUsername());
        sysUserMapper.insert(sysUser);
        String uid = sysUser.getId();
        // 添加岗位关联关系
        List<String> positionIds = sysUserDto.getPositionIds();
        if (IterUtil.isNotEmpty(positionIds)) {
            List<SysUserPosition> sysUserPositionList = new ArrayList<>();
            for (String positionId : positionIds) {
                SysUserPosition sysUserPosition = new SysUserPosition();
                sysUserPosition.setUid(uid);
                sysUserPosition.setPid(positionId);
                sysUserPositionList.add(sysUserPosition);
            }
            sysUserPositionService.saveBatch(sysUserPositionList);
        }

        // 添加用户角色关联关系
        List<String> roleIds = sysUserDto.getRoleIds();
        if (IterUtil.isNotEmpty(roleIds)) {
            List<SysRoleUser> sysRoleUserList = new ArrayList<>();
            for (String roleId : roleIds) {
                SysRoleUser sysRoleUser = new SysRoleUser();
                sysRoleUser.setUid(uid);
                sysRoleUser.setRid(roleId);
                sysRoleUserList.add(sysRoleUser);
            }
            sysRoleUserService.saveBatch(sysRoleUserList);
        }

    }


    /**
     * 修改
     *
     * @param sysUserDto 入参
     * @author gltqe
     * @date 2023/5/19 9:26
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserDto sysUserDto, MultipartFile avatarFile) {
        SysUser sysUser = BeanUtil.copyProperties(sysUserDto, SysUser.class);
        // 禁止通过修改用户 修改密码 用户名
        sysUser.setUsername(null);
        sysUser.setPassword(null);
        // 保存头像
        String avatar = null;
        if (avatarFile != null) {
            FileInfo fileInfo = fileInfoService.upload(avatarPath, avatarFile, Constant.AVATAR_SIZE);
            avatar = fileInfo.getId();
        }
        sysUser.setAvatar(avatar);
        sysUserMapper.updateById(sysUser);
        String uid = sysUser.getId();
        // 岗位
        List<String> positionIds = sysUserDto.getPositionIds();
        if (positionIds != null) {
            // 删除用户岗位关系
            LambdaQueryWrapper<SysUserPosition> upWrapper = new LambdaQueryWrapper<>();
            upWrapper.eq(SysUserPosition::getUid, uid);
            sysUserPositionMapper.delete(upWrapper);
            // 添加用户岗位关联关系
            if (!positionIds.isEmpty()) {
                List<SysUserPosition> sysUserPositionList = new ArrayList<>();
                for (String positionId : positionIds) {
                    SysUserPosition sysUserPosition = new SysUserPosition();
                    sysUserPosition.setUid(uid);
                    sysUserPosition.setPid(positionId);
                    sysUserPositionList.add(sysUserPosition);
                }
                sysUserPositionService.saveBatch(sysUserPositionList);
            }
        }
        // 角色
        List<String> roleIds = sysUserDto.getRoleIds();
        if (roleIds != null) {
            // 删除用户角色关联关系
            LambdaQueryWrapper<SysRoleUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysRoleUser::getUid, uid);
            sysRoleUserService.remove(wrapper);
            // 添加用户角色关联关系
            if (!roleIds.isEmpty()) {
                List<SysRoleUser> sysRoleUserList = new ArrayList<>();
                for (String roleId : roleIds) {
                    SysRoleUser sysRoleUser = new SysRoleUser();
                    sysRoleUser.setUid(uid);
                    sysRoleUser.setRid(roleId);
                    sysRoleUserList.add(sysRoleUser);
                }
                sysRoleUserService.saveBatch(sysRoleUserList);
            }
        }
    }

    /**
     * 获取详细信息
     *
     * @param sysUserDto 入参
     * @return com.gltqe.wladmin.system.entity.po.SysUser
     * @author gltqe
     * @date 2022/7/3 2:10
     **/
    @Override
    public SysUserVo getOneUser(SysUserDto sysUserDto) {
        String uid = sysUserDto.getId();
        SysUser sysUser = sysUserMapper.selectById(uid);
        SysUserVo sysUserVo = BeanUtil.copyProperties(sysUser, SysUserVo.class);
        // 角色
        LambdaQueryWrapper<SysRoleUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleUser::getUid, uid);
        List<SysRoleUser> sysRoleUserList = sysRoleUserMapper.selectList(wrapper);
        List<String> rids = sysRoleUserList.stream().map(SysRoleUser::getRid).collect(Collectors.toList());
        sysUserVo.setRoleIds(rids);
        // 职业
        LambdaQueryWrapper<SysUserPosition> upWrapper = new LambdaQueryWrapper<>();
        upWrapper.eq(SysUserPosition::getUid, uid);
        List<SysUserPosition> sysUserPositionList = sysUserPositionMapper.selectList(upWrapper);
        List<String> pids = sysUserPositionList.stream().map(SysUserPosition::getPid).collect(Collectors.toList());
        sysUserVo.setPositionIds(pids);
        // 头像
        FileInfo info = fileInfoService.getById(sysUserVo.getAvatar());
        sysUserVo.setAvatarName(info != null ? info.getUuidName() : null);
        return sysUserVo;
    }

    /**
     * 修改密码-个人信息页
     *
     * @param passwordDto 入参
     * @author gltqe
     * @date 2022/7/3 2:10
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSelfPassword(PasswordDto passwordDto) {
        String old = passwordDto.getOld();
        String aNew = passwordDto.getNew1();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SysUser::getId, SysUser::getPassword).eq(SysUser::getUsername, JwtUtil.getUsername());
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        String oldPasswordEncode = sysUser.getPassword();
        boolean matches = passwordEncoder.matches(old, oldPasswordEncode);
        if (!matches) {
            throw new WlException("旧密码错误,请重新输入!");
        }
        String encode = passwordEncoder.encode(aNew);
        SysUser user = new SysUser();
        user.setId(sysUser.getId());
        user.setPassword(encode);
        sysUserMapper.updateById(user);
    }

    /**
     * 修改密码-用户列表页
     * // todo 权限
     *
     * @param passwordDto 入参
     * @author gltqe
     * @date 2022/7/3 2:11
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOtherPassword(PasswordDto passwordDto) {
        String uid = passwordDto.getUid();
        String aNew = passwordDto.getNew1();
        String encode = passwordEncoder.encode(aNew);
        SysUser user = new SysUser();
        user.setId(uid);
        user.setPassword(encode);
        sysUserMapper.updateById(user);
    }

    /**
     * 获取个人信息
     *
     * @return com.gltqe.wladmin.system.entity.po.SysUser
     * @author gltqe
     * @date 2022/7/3 2:11
     **/
    @Override
    public SysUserVo getSelfInfo() {
        String userId = JwtUtil.getUserId();
        SysUserVo sysUserVo = sysUserMapper.getSelfInfo(userId);
        if (StringUtils.isNotBlank(sysUserVo.getAvatar())) {
            FileInfo fileInfo = fileInfoService.getById(sysUserVo.getAvatar());
            sysUserVo.setAvatarName(fileInfo.getUuidName());
        }
        return sysUserVo;
    }

    /**
     * 修改个人信息
     *
     * @param sysUserDto 用户信息
     * @param multipartFile 头像
     * @author gltqe
     * @date 2022/7/3 2:12
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSelf(SysUserDto sysUserDto, MultipartFile multipartFile) {
        String cnName = sysUserDto.getCnName();
        Integer age = sysUserDto.getAge();
        String sex = sysUserDto.getSex();
        String phone = sysUserDto.getPhone();
        String email = sysUserDto.getEmail();
        String profile = sysUserDto.getProfile();
        String remarks = sysUserDto.getRemarks();
        String avatar = null;
        if (multipartFile != null) {
            FileInfo fileInfo = fileInfoService.upload(avatarPath, multipartFile, Constant.AVATAR_SIZE);
            avatar = fileInfo.getId();
        }
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(StringUtils.isNotBlank(cnName), SysUser::getCnName, cnName)
                .set(age != null, SysUser::getAge, age)
                .set(StringUtils.isNotBlank(sysUserDto.getSex()), SysUser::getSex, sex)
                .set(StringUtils.isNotBlank(phone), SysUser::getPhone, phone)
                .set(StringUtils.isNotBlank(email), SysUser::getEmail, email)
                .set(StringUtils.isNotBlank(profile), SysUser::getProfile, profile)
                .set(StringUtils.isNotBlank(remarks), SysUser::getRemarks, remarks)
                .set(StringUtils.isNotBlank(avatar), SysUser::getAvatar, avatar)
                .eq(SysUser::getStatus, UserStatusEnum.NORMAL.getCode())
                .eq(SysUser::getUsername, JwtUtil.getUsername());
        sysUserMapper.update(null, wrapper);
    }

    private void checkUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new WlException("用户名不能为空");
        }

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username)
                .eq(SysUser::getIsDelete, Constant.N);
        Long count = sysUserMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new WlException("用户名重复");
        }
    }

    @Override
    public void exportUser(SysUserDto sysUserDto, HttpServletResponse response) {
        // 临时修改 todo
        List<SysUserVo> list = new ArrayList<>();
        List<SysUser> list1 = list();
        for (SysUser sysUser : list1) {
            SysUserVo sysUserVo = new SysUserVo();
            BeanUtils.copyProperties(sysUser,sysUserVo);
            list.add(sysUserVo);
        }
        ExcelUtil.writeExcel(list, "用户信息", "用户信息sheet", response, SysUserVo.class);
    }
}
