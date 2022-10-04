package com.itheima.reggie.exception;


import com.itheima.reggie.common.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description 全局异常处理器
 * @data 2022/10/2 15 : 35 : 04
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalHandleException {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> handleException(SQLIntegrityConstraintViolationException exception){
        if(exception.getMessage().contains("Duplicate entry")){
            String[] exs = exception.getMessage().split(" ");
            return R.error("账号:"+exs[2]+"已存在！请换个账号试试。");
        }
        return R.error("未知错误！请重试。");
    }
    @ExceptionHandler(CustomException.class)
    public R<String> runtimeException(CustomException cex){
        return R.error(cex.getMessage());
    }
}
