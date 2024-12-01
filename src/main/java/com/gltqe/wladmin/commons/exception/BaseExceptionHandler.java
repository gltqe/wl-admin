package com.gltqe.wladmin.commons.exception;


import com.gltqe.wladmin.commons.common.Constant;
import com.gltqe.wladmin.commons.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类 优先级低于在controller中的异常处理方法
 *
 * @author gltqe
 * @date 2022/7/3 0:36
 **/
@Slf4j
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = LimitException.class)
    @ResponseBody
    public Result apiLimit(LimitException e) {
        log.error(e.getMessage());
        return Result.error(Constant.CODE_500, e.getMessage());
    }


    @ExceptionHandler(value = QuartzException.class)
    @ResponseBody
    public Result quartz(QuartzException e) {
        log.error(e.getMessage());
        return Result.error(Constant.CODE_500, e.getMessage());
    }


    @ExceptionHandler(value = LoginException.class)
    @ResponseBody
    public Result login(LoginException e) {
        log.error(e.getMessage());
        return Result.error(Constant.CODE_401, e.getMessage());
    }

    @ExceptionHandler(value = TokenErrorException.class)
    @ResponseBody
    public Result tokenError(TokenErrorException e) {
        log.error(e.getMessage());
        return Result.error(Constant.CODE_1000, e.getMessage());
    }

    @ExceptionHandler(value = TokenExpireException.class)
    @ResponseBody
    public Result tokenExpire(TokenExpireException e) {
        log.error(e.getMessage());
        return Result.error(Constant.CODE_1001, e.getMessage());
    }

    @ExceptionHandler(value = TokenException.class)
    @ResponseBody
    public Result token(TokenException e) {
        log.error(e.getMessage());
        return Result.error(Constant.CODE_403, e.getMessage());
    }

    @ExceptionHandler(value = PermissionException.class)
    @ResponseBody
    public Result permission(PermissionException e) {
        log.error(e.getMessage());
        return Result.error(Constant.CODE_403, "未授权，无法访问该接口！");
    }

    @ExceptionHandler(value = WlException.class)
    @ResponseBody
    public Result wl(WlException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(value = FileException.class)
    @ResponseBody
    public Result error(FileException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result error(MethodArgumentNotValidException e) {
        // 参数效验异常
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.error( e.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Result error(BindException e) {
        // 参数效验异常
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.error( e.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return Result.error("未知异常，请联系管理员");
    }
}
