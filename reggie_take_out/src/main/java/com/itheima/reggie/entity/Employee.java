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
 * @description 员工实体类
 * @data 2022/9/30 20 : 14 : 42
 */
@Data
public class Employee implements Serializable {
    private  static final long serialVersionUID=1L;
    private Long id;
    private String name;
    private String username;
    private String password;
    private String phone;
    private String sex;
    private String idNumber;//身份证号
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableField(fill= FieldFill.INSERT)
    private Long createUser;
    @TableField(fill=FieldFill.UPDATE)
    private Long updateUser;

}
