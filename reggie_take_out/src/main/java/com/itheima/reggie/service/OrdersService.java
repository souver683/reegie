package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description
 * @data 2022/10/13 10 : 17 : 47
 */
public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
