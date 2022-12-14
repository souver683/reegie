package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description
 * @data 2022/10/5 18 : 46 : 04
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping
    public R<String> addDish(@RequestBody DishDto dishDto){
            dishService.saveFlavor(dishDto);
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("添加菜品成功！");
    }
    @GetMapping("/page")
    private R<Page> page(int page,int pageSize,String name){
        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> dtoPage=new Page<>();
        LambdaQueryWrapper<Dish> lqw=new LambdaQueryWrapper<>();
        lqw.like(name!=null,Dish::getName,name);
        lqw.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,lqw);
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list=new ArrayList<>();
        records.forEach((item)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String name1 = category.getName();
            dishDto.setCategoryName(name1);
            list.add(dishDto);
        });
//        List<DishDto>   list=   records.stream().map((item)->{
//            DishDto dishDto=new DishDto();
//            BeanUtils.copyProperties(item,dishDto);
//            Long categoryId = item.getCategoryId();
//            Category category = categoryService.getById(categoryId);
//            String name1 = category.getName();
//            dishDto.setCategoryName(name1);
//            return dishDto;
//        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 根据id查看菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> alter(@PathVariable Long id){
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);
        return  R.success(byIdWithFlavor);
    }
    @PutMapping
    public R<String> updated(@RequestBody DishDto dishDto){
            dishService.updateDish(dishDto);
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("修改成功！");
    }
//    @GetMapping("/list")
//    public R<List<Dish>> selectDish(Dish dish){
//        LambdaQueryWrapper<Dish> lqw=new LambdaQueryWrapper<>();
//        lqw.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId()).eq(Dish::getStatus,1);
//        lqw.eq(Dish::getStatus,1);
//        lqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(lqw);
//        return R.success(list);
//    }
@GetMapping("/list")
public R<List<DishDto>> selectDish(Dish dish){
    List<DishDto> listDto;
        //将菜品储存都redis中
    String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
    listDto=(List<DishDto>)redisTemplate.opsForValue().get(key);
    if (listDto!=null){
        return R.success(listDto);
    }

    LambdaQueryWrapper<Dish> lqw=new LambdaQueryWrapper<>();
    log.info("dish------>{}",dish);
    lqw.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId()).eq(Dish::getStatus,1);
    lqw.eq(Dish::getStatus,1);
    lqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
    List<Dish> list = dishService.list(lqw);
    listDto= list.stream().map((item)->{
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(item,dishDto);
        Long categoryId = item.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            String name = category.getName();
            dishDto.setCategoryName(name);
        }
        Long dishId = item.getId();
        LambdaQueryWrapper<DishFlavor> lqwflavor=new LambdaQueryWrapper<>();
        lqwflavor.eq(DishFlavor::getDishId,dishId);
        List<DishFlavor> dishFlavors = dishFlavorService.list(lqwflavor);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }).collect(Collectors.toList());

    redisTemplate.opsForValue().set(key,listDto,1, TimeUnit.HOURS);
    return R.success(listDto);
}

    @PostMapping("/status/{sta}")
    public R<String> status(@PathVariable int sta ,@RequestParam List<Long> ids) {

        List<Dish> list = dishService.listByIds(ids);
        for (Dish dish : list) {
            if(dish.getStatus()==sta){
                return R.error("当前状态不可改变");
            }
            else{
                dish.setStatus(sta);
                dishService.updateById(dish);
            }

        }
        return R.success("修改成功！");
    }
    @DeleteMapping
    public R<String> delete(Long[] ids){
        for (Long id : ids) {
           dishService.removeById(id);
        }
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);
        return R.success("删除成功！");
    }

}
