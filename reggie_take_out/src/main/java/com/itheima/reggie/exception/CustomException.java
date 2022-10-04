package com.itheima.reggie.exception;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description 菜品分类与套餐分类异常处理器
 * @data 2022/10/4 16 : 34 : 23
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
