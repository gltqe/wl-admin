package com.gltqe.wladmin.framework.mybatisPlus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.gltqe.wladmin.framework.datascope.DataScopeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus 配置
 *
 * @author gltqe
 * @date 2022/7/3 0:38
 **/
@Configuration
public class MybatisPlusConfig {

    @Autowired
    private DataScopeInterceptor dataScopeInterceptor;

    /**
     * 插件 （数据权限插件 分页插件）
     *
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     * @author gltqe
     * @date 2022/7/3 0:38
     **/
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 添加数据权限插件
        DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor();

        // 添加自定义的数据权限处理器
        dataPermissionInterceptor.setDataPermissionHandler(dataScopeInterceptor);
        interceptor.addInnerInterceptor(dataPermissionInterceptor);

        // 分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        return interceptor;
    }

    private PaginationInnerInterceptor paginationInnerInterceptor(){
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 分页最大返回条数100条
        paginationInnerInterceptor.setMaxLimit(100L);
        // 溢出修正
        paginationInnerInterceptor.setOverflow(true);
        return paginationInnerInterceptor;
    }

}
