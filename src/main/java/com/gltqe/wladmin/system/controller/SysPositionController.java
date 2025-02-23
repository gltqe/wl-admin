package com.gltqe.wladmin.system.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.common.Result;
import com.gltqe.wladmin.system.entity.dto.SysPositionDto;
import com.gltqe.wladmin.system.entity.po.SysPosition;
import com.gltqe.wladmin.system.entity.vo.SysPositionVo;
import com.gltqe.wladmin.system.service.SysPositionService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 职位
 *
 * @author gltqe
 * @date 2022/7/3 1:30
 **/
@RestController
@RequestMapping("/sysPosition")
public class SysPositionController {
    @Resource
    private SysPositionService sysPositionService;

    /**
     * 分页查询
     *
     * @param sysPositionDto
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:31
     **/
    @RequestMapping("/page")
    @PreAuthorize("@am.hasPermission('position:list:query')")
    public Result page(@RequestBody SysPositionDto sysPositionDto) {
        IPage<SysPosition> iPage = sysPositionService.page(sysPositionDto);
        return Result.ok(iPage);
    }

    /**
     * 列表
     *
     * @return com.gltqe.wladmin.commons.common.Result
     * @author gltqe
     * @date 2022/7/3 1:31
     **/
    @RequestMapping("/list")
    @PreAuthorize("@am.hasPermission('position:list:query')")
    public Result list() {
        LambdaQueryWrapper<SysPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPosition::getStatus, Constant.N);
        List<SysPosition> list = sysPositionService.list(wrapper);
        return Result.ok(list);
    }

    /**
     * 修改状态
     *
     * @param sysPositionDto
     * @author gltqe
     * @date 2024/9/16 11:08
     * @return: com.gltqe.wladmin.commons.common.Result
     */
    @RequestMapping("/updateStatus")
    @PreAuthorize("@am.hasPermission('position:list:update')")
    public Result updateStatus(@RequestBody SysPositionDto sysPositionDto) {
        sysPositionService.updateStatus(sysPositionDto);
        return Result.ok();
    }

    /**
     * 新增职位
     *
     * @param sysPositionDto
     * @author gltqe
     * @date 2024/9/16 11:08
     * @return: com.gltqe.wladmin.commons.common.Result
     */
    @RequestMapping("/add")
    @PreAuthorize("@am.hasPermission('position:list:add')")
    public Result add(@RequestBody SysPositionDto sysPositionDto) {
        sysPositionService.addPosition(sysPositionDto);
        return Result.ok();
    }

    /**
     * 详情
     *
     * @param sysPositionDto
     * @author gltqe
     * @date 2024/9/16 11:09
     * @return: com.gltqe.wladmin.commons.common.Result
     */
    @RequestMapping("/getOne")
    @PreAuthorize("@am.hasPermission('position:list:query')")
    public Result getOne(@RequestBody SysPositionDto sysPositionDto) {
        SysPosition sysPosition = sysPositionService.getById(sysPositionDto.getId());
        SysPositionVo sysPositionVo = BeanUtil.copyProperties(sysPosition, SysPositionVo.class);
        return Result.ok(sysPositionVo);
    }

    /**
     * 修改
     *
     * @param sysPositionDto
     * @author gltqe
     * @date 2024/9/16 11:09
     * @return: com.gltqe.wladmin.commons.common.Result
     */
    @RequestMapping("/update")
    @PreAuthorize("@am.hasPermission('position:list:update')")
    public Result update(@RequestBody SysPositionDto sysPositionDto) {
        sysPositionService.updatePosition(sysPositionDto);
        return Result.ok();
    }

    /**
     * 删除
     *
     * @param ids
     * @author gltqe
     * @date 2024/9/16 11:09
     * @return: com.gltqe.wladmin.commons.common.Result
     */
    @RequestMapping("/remove")
    @PreAuthorize("@am.hasPermission('position:list:remove')")
    public Result remove(@RequestBody List<String> ids) {
        sysPositionService.removePosition(ids);
        return Result.ok("删除成功");
    }

    /**
     * 导出
     * @author gltqe
     * @date 2024/5/13 18:03
     * @param sysPositionDto
     * @param response
     * return
     */
    @RequestMapping("/exportPosition")
    public void exportUser(@RequestBody SysPositionDto sysPositionDto, HttpServletResponse response){
        sysPositionService.exportPosition(sysPositionDto,response);
    }
}
