package com.gltqe.wladmin.framework.mybatisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
/**
 * 自定义自动填充
 * @author gltqe
 * @date 2022/7/3 1:06
 **/
@Configuration
public class CustomMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createTime", new Date(), metaObject);
        setFieldValByName("createId", JwtUtil.getUserId(),metaObject);
        setFieldValByName("updateTime",new Date(),metaObject);
        setFieldValByName("updateId",  JwtUtil.getUserId(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime",new Date(),metaObject);
        setFieldValByName("updateId", JwtUtil.getUserId(),metaObject);
    }
}
