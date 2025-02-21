package com.gltqe.wladmin.commons.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gltqe.wladmin.commons.enums.ExportTypeEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求参数基类
 *
 * @author gltqe
 * @date 2023/5/18 9:13
 **/
@Data
public class BaseDto<T> {


    private String id;

    /**
     * 页数
     **/
    private Integer currentPage;

    /**
     * 每页大小
     **/
    private Integer pageSize;

    /**
     * 排序 true 升序 false 降序
     **/
    private Boolean isAsc;

    /**
     * 排序字段
     **/
    private String sortField;

    /**
     * 开始时间
     */
    private String startDateTime;

    /**
     * 结束时间
     */
    private String endDateTime;

    /**
     * 导出类型 0 当前分页 1 当前查询(默认) 2 全部数据 3 勾选数据
     **/
    private Integer exportType;

    /**
     * 勾选数据的ID
     **/
    private List<String> ids;


    /**
     * 备注
     **/
    private String remarks;


    /**
     * 分页方法
     **/
    public Page<T> getPageNotOrders() {
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Page<T> page = new Page<>(currentPage, pageSize);
        return page;
    }

    /**
     * 排序方法
     **/
    public List<OrderItem> getOrders() {
        List<OrderItem> list = new ArrayList<>();
        if (StringUtils.isNotBlank(sortField) && isAsc != null) {
            OrderItem orderItem = new OrderItem();
            orderItem.setAsc(isAsc);
            orderItem.setColumn(StrUtil.toUnderlineCase(sortField));
            list.add(orderItem);
        }
        return list;
    }

    /**
     * 分页+排序
     **/
    public Page<T> getPage() {
        Page<T> page = getPageNotOrders();
        page.setOrders(getOrders());
        return page;
    }

    public Integer getExportType() {
        if (exportType != null) {
            return exportType;
        } else {
            return ExportTypeEnum.QUERY_ALL.getCode();
        }
    }
}
