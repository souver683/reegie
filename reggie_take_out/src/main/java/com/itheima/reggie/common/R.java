package com.itheima.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description 通用返回结果，服务器响应的最终结果都会封装成此对象
 * @data 2022/9/30 20 : 36 : 41
 */
@Data
public class R<T> {
    private Integer code;
    private String msg;
    private T data;
    private Map map=new HashMap();//动态数据
    public static <T> R<T>  success(T object){
        R<T> r =new R<>();
        r.data=object;
        r.code=1;
        return r;
    }
    public static <T> R<T>  error(String msg){
        R r =new R ();
        r.msg= msg;
        r.code=0;
        return r;
    }
    public R<T> add(String key,Object value){
        this.map.put(key,value);
        return this;
    }
}
