package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import org.springframework.stereotype.Service;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description
 * @data 2022/10/4 15 : 59 : 04
 */

public interface DishService extends IService<Dish> {
    void saveFlavor(DishDto dishDto);
    DishDto getByIdWithFlavor(Long id);
    public void updateDish(DishDto dishDto);
}
