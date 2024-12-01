package com.gltqe.wladmin.monitor.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.system.entity.dto.LoginDetailDto;
import com.gltqe.wladmin.monitor.entity.vo.OnlineUserVo;
import com.gltqe.wladmin.monitor.entity.dto.OnlineUserDto;
import com.gltqe.wladmin.monitor.service.OnlineUserService;
import com.gltqe.wladmin.system.entity.po.SysDept;
import com.gltqe.wladmin.system.entity.po.SysUser;
import com.gltqe.wladmin.system.entity.bo.UserDetailsBo;
import com.gltqe.wladmin.system.mapper.SysDeptMapper;
import com.gltqe.wladmin.system.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OnlineUserServiceImpl implements OnlineUserService {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public IPage<OnlineUserVo> page(OnlineUserDto onlineUserDto) {
        Set keys = redisTemplate.keys(Constant.LOGIN_USER_KEY + "*");
        // 当前在线总人数
        int size = keys.size();
        String userId = onlineUserDto.getUserId();
        String deptId = onlineUserDto.getDeptId();
        String ip = onlineUserDto.getIp();
        // 获取所有能看到的用户 TODO 权限
//        sysUserMapper.selectList();

        List<SysUser> listByUser = sysUserMapper.getListByUser();
        Map<String, String> tempUser = new HashMap<>();
        List<String> userIds = new ArrayList<>();
        for (SysUser sysUser : listByUser) {
            tempUser.put(sysUser.getId(), sysUser.getCnName());
            userIds.add(sysUser.getId());
        }

        TreeMap<Long, UserDetailsBo> treeMap = new TreeMap<>(Comparator.reverseOrder());
        for (Object key : keys) {
            boolean flag = true;
            Object object = redisTemplate.opsForValue().get(key);
            UserDetailsBo o = JSONObject.parseObject(JSONObject.toJSONString(object), UserDetailsBo.class);
            Long expire = redisTemplate.getExpire(key);

            if (StringUtils.isNotBlank(onlineUserDto.getUsername()) && !o.getUsername().contains(onlineUserDto.getUsername())) {
                flag = false;
            }

            if (StringUtils.isNotBlank(userId) && !userId.equals(o.getUserId())) {
                flag = false;
            }

            if (StringUtils.isNotBlank(deptId) && !deptId.equals(o.getDeptId())) {
                flag = false;
            }

            if (StringUtils.isNotBlank(ip) && o.getLoginDetail() != null && StringUtils.isNotBlank(o.getLoginDetail().getIp())) {
                if (!o.getLoginDetail().getIp().contains(ip)) {
                    flag = false;
                }
            }
            if (!userIds.contains(o.getUserId())) {
                flag = false;
            }

            if (flag) {
                treeMap.put(expire, o);
            }
        }
        List<OnlineUserVo> onlineUserDtoList = new ArrayList<>();
        Integer pageSize = onlineUserDto.getPageSize();
        Integer currentPage = onlineUserDto.getCurrentPage();
        Integer offset = (currentPage - 1) * pageSize;
        Set<String> ids = new HashSet<>();
        int count = 0;
        for (Map.Entry<Long, UserDetailsBo> entry : treeMap.entrySet()) {
            if (count >= offset && count < offset + pageSize) {
                UserDetailsBo value = entry.getValue();
                OnlineUserVo onlineUserVo = setDto(value);
                onlineUserDtoList.add(onlineUserVo);
                if (StringUtils.isNotBlank(value.getDeptId())) {
                    ids.add(value.getDeptId());
                }
            }
            count++;
            if (onlineUserDtoList.size() == pageSize) {
                break;
            }
        }

        // 设置部门名称
        Map<String, String> tempDept = new HashMap<>();
        if (!ids.isEmpty()) {
            List<SysDept> deptList = sysDeptMapper.selectBatchIds(ids);

            for (SysDept sysDept : deptList) {
                tempDept.put(sysDept.getId(), sysDept.getName());
            }
        }
        for (OnlineUserVo onlineUserVo : onlineUserDtoList) {
            onlineUserVo.setDeptName(tempDept.get(onlineUserDto.getDeptId()));
            onlineUserVo.setCnName(tempUser.get(onlineUserDto.getUserId()));
        }
        IPage<OnlineUserVo> iPage = new Page<>();
        iPage.setTotal(size);
        iPage.setRecords(onlineUserDtoList);
        return iPage;
    }

    @Override
    public void exit(OnlineUserDto onlineUserVo) {
        String username = onlineUserVo.getUsername();
        redisTemplate.delete(Constant.LOGIN_USER_KEY + username);
    }


    private OnlineUserVo setDto(UserDetailsBo value) {
        OnlineUserVo onlineUserVo = new OnlineUserVo();
        onlineUserVo.setUserId(value.getUserId());
        onlineUserVo.setUsername(value.getUsername());
        onlineUserVo.setStatus(value.getStatus());
        onlineUserVo.setDeptId(value.getDeptId());
        LoginDetailDto loginDetail = value.getLoginDetail();
        if (loginDetail != null) {
            onlineUserVo.setLoginTime(loginDetail.getLoginTime());
            onlineUserVo.setBrowser(loginDetail.getBrowser());
            onlineUserVo.setIp(loginDetail.getIp());
            onlineUserVo.setOs(loginDetail.getOs());
        }
        return onlineUserVo;
    }
}
