package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description
 * @data 2022/10/13 10 : 23 : 32
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
            ordersService.submit(orders);
        return R.success("支付成功！");
    }
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize){
        log.info("page{}",pageSize);
        Page<Orders> pages =new Page<>(page,pageSize);
//        LambdaQueryWrapper<Orders> lqw=new LambdaQueryWrapper<>();
//        lqw.orderByAsc(Orders::getAmount);
        ordersService.page(pages,null);

        return R.success(pages);
    }
}
