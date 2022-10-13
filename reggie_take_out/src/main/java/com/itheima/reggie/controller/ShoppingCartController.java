package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description
 * @data 2022/10/12 13 : 34 : 29
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> lqw=new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        lqw.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(lqw);
        return R.success(list);
    }
    @PostMapping("/add")
    public R<ShoppingCart> addcart(@RequestBody ShoppingCart shoppingCart){
        //设置用户id
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //查询当前菜品或者套餐是否在购物车当中
        LambdaQueryWrapper<ShoppingCart> lqw=new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        if(shoppingCart.getDishId()!=null){
            lqw.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        else{
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart serviceOne = shoppingCartService.getOne(lqw);
        if(serviceOne!=null){
            Integer number = serviceOne.getNumber();
            serviceOne.setNumber(number+1);
            shoppingCartService.updateById(serviceOne);
        }
        else{
            //已存在在当前菜品数量上加一不存在 就新增到购物车
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            serviceOne=shoppingCart;
        }

        return R.success(serviceOne);
    }
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> lqw=new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        if(shoppingCart.getDishId()!=null){
            Long dishId = shoppingCart.getDishId();
            lqw.eq(ShoppingCart::getDishId,dishId);
        }
        else{
            Long setmealId = shoppingCart.getSetmealId();
            lqw.eq(ShoppingCart::getSetmealId,setmealId);
        }

        ShoppingCart serviceOne = shoppingCartService.getOne(lqw);
        if(serviceOne.getNumber()>0){
            Integer number = serviceOne.getNumber();
            serviceOne.setNumber(number-1);
            shoppingCartService.updateById(serviceOne);
            log.info("number{}",serviceOne.getNumber());
        }

        if (serviceOne.getNumber()<=0){
            shoppingCartService.remove(lqw);
        }


        return R.success("删除成功");
    }
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> lqw=new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(lqw);
        return R.success("购物车清空成功！");
    }

}
