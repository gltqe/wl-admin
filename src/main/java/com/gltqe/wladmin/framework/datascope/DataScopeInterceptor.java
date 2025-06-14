package com.gltqe.wladmin.framework.datascope;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.utils.JwtUtil;
import com.gltqe.wladmin.system.entity.po.SysRole;
import com.gltqe.wladmin.system.entity.bo.UserDetailsBo;
import lombok.SneakyThrows;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Column;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;

/**
 * 数据权限拦截器 //这里只能使用Component 不能使用Configuration
 *
 * @author gltqe
 * @date 2022/7/3 0:58
 **/
@Aspect
@Component
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}))
public class DataScopeInterceptor implements DataPermissionHandler {

    ThreadLocal<DataScopeParam> threadLocal = new ThreadLocal<>();

    /**
     * 配置织入点
     *
     * @author gltqe
     * @date 2022/7/3 0:58
     **/
    @Pointcut("@annotation(com.gltqe.wladmin.framework.datascope.DataScope)")
    public void dataScope() {
    }

    /**
     * 清空当前线程上次保存的权限信息
     *
     * @author gltqe
     * @date 2022/7/3 1:00
     **/
    @After("dataScope()")
    public void clearThreadLocal() {
        threadLocal.remove();
    }

    /**
     * 配置权限
     *
     * @param pjp
     * @author gltqe
     * @date 2022/7/3 1:00
     **/
    @Before("dataScope()")
    public void dataScopeBefore(JoinPoint pjp) {
        // 获取注解
        DataScope ds = getAnnotation(pjp);
        DataScopeParam dataScopeParam = new DataScopeParam();
        if (ds == null || (StringUtils.isBlank(ds.dt()) && StringUtils.isBlank(ds.ut()))) {
            threadLocal.remove();
            return;
        }
        dataScopeParam.setUt(ds.ut());
        dataScopeParam.setUf(ds.uf());
        dataScopeParam.setDt(ds.dt());
        dataScopeParam.setDf(ds.df());
        threadLocal.set(dataScopeParam);
    }

    private DataScope getAnnotation(JoinPoint pjp) {
        org.aspectj.lang.Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        DataScope annotation = method.getAnnotation(DataScope.class);
        return annotation;
    }

    @SneakyThrows
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        DataScopeParam dataScopeParam = threadLocal.get();
        if (dataScopeParam == null) {
            return where;
        }
        UserDetailsBo userDetails = JwtUtil.getUserDetails();
        String username = userDetails.getUsername();
        if (JwtUtil.isAdmin(username)) {
            return where;
        }
        String sql = getSql(dataScopeParam, userDetails);
        if (StringUtils.isBlank(sql)) {
            return where;
        }
        if (where == null) {
            return new Column(sql);
        }
        AndExpression andExpression = new AndExpression(where, new Column(sql));
        return andExpression;
    }


    private String getSql(DataScopeParam dataScopeParam, UserDetailsBo userDetails) {
        List<SysRole> roleList = userDetails.getRoleList();
        String userId = userDetails.getUserId();
        String deptId = userDetails.getDeptId();

        String ut = dataScopeParam.getUt();
        String uf = dataScopeParam.getUf();
        String dt = dataScopeParam.getDt();
        String df = dataScopeParam.getDf();
        // 数据表的用户字段
        String utf = StringUtils.isBlank(ut) ? null : ut + "." + uf;
        // 数据表的部门字段
        String dtf = StringUtils.isBlank(dt) ? null : dt + "." + df;
        StringBuilder sql = new StringBuilder();
        for (SysRole sysRole : roleList) {
            String dataScope = sysRole.getDataScope();

            if (Constant.SELF.equals(dataScope)) {
                // 个人权限
                sql.append(" OR ");
                if (StringUtils.isNotBlank(ut)) {
                    sql.append(utf).append(" = ").append(userId);
                } else {
                    sql.append(" 1=0 ");
                }
            } else if (Constant.ALL.equals(dataScope)) {
                // 拥有全部数据权限
                sql = new StringBuilder();
                break;
            } else if (Constant.DEPT.equals(dataScope)) {
                // 部门权限
                sql.append(" OR ").append(dtf).append(" = ").append(deptId);

            } else if (Constant.DEPT_DOWN.equals(dataScope)) {
                // 本部门及以下
                sql.append(" OR ")
                        .append(dtf)
                        .append(" in ")
                        .append(" (with recursive dept_temp as ( select sd.id from sys_dept sd where sd.id = '")
                        .append(deptId)
                        .append("' union all select sd.id from sys_dept sd,dept_temp where sd.parent_id = dept_temp.id ) select id from dept_temp) ");

            } else if (Constant.CUSTOM.equals(dataScope)) {
                // 自定义权限
                sql.append(" OR ")
                        .append(dtf)
                        .append(" in ")
                        .append(" ( select sys_role_dept.did from sys_role_dept where sys_role_dept.rid='")
                        .append(sysRole.getId())
                        .append("')");

            }
        }
        String s = sql.toString().replaceFirst(" OR ", " ");
        if (StringUtils.isNotBlank(s)) {
            s = "(" + s + ")";
        }
        return s;
    }

}
