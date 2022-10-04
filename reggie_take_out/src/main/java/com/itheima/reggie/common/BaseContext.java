package com.itheima.reggie.common;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description 将session中的id 放到同一个线程中
 * @data 2022/10/3 20 : 21 : 18
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
