package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description 分类
 * @data 2022/10/3 21 : 14 : 32
 */
@Data
public class Category implements Serializable {
    private static final long serialVersionUID=1L;
    private Long id;
    //类型 1.菜品分类 2.套餐分类
    private Integer type;
    //菜品分类
    private String name;
    //展示顺序
    private Integer sort;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
