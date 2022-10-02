package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description employee的实体映射
 * @data 2022/9/30 20 : 26 : 14
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
