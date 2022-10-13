package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description 地址
 * @data 2022/10/9 16 : 53 : 39
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
