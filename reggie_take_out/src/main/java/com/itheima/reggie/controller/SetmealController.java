package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description 套餐管理
 * @data 2022/10/6 20 : 12 : 28
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;
    @PostMapping
    public R<String> saveDishSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("保存成功！");
    }
    @GetMapping("/page")
    public R<Page> Page(int page,int pageSize,String name){
        Page<Setmeal> setmealPage=new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage=new Page<>();
        LambdaQueryWrapper<Setmeal> lqw=new LambdaQueryWrapper<>();
        lqw.like(name!=null,Setmeal::getName,name);
        lqw.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, lqw);
        BeanUtils.copyProperties(setmealPage,dtoPage,"records");

        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list=new ArrayList<>();
        records.forEach((item)->{
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long id = item.getCategoryId();
            Category categoryId = categoryService.getById(id);
            String name1 = categoryId.getName();
            setmealDto.setCategoryName(name1);
            list.add(setmealDto);
        });
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }
    @DeleteMapping
    public R<String> deleteSetmeal(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("删除成功！");
    }
    @GetMapping("/{id}")
    public R<SetmealDto> getByid(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getSetmeal(id);
        return R.success(setmealDto);
    }
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("修改成功！");
    }
    @PostMapping("/status/{stat}")
    public R<String> updateStasu(@PathVariable int stat,@RequestParam List<Long> ids){
        List<Setmeal> setmeals = setmealService.listByIds(ids);
        for (Setmeal setmeal : setmeals) {
            if(setmeal.getStatus()==stat){
                return R.error("当前状态不可修改！");
            }
            else{
                setmeal.setStatus(stat);
                setmealService.updateById(setmeal);
            }
        }
        return R.success("删除成功！");
    }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> lqw=new LambdaQueryWrapper<>();
        lqw.eq(setmeal!=null,Setmeal::getCategoryId,setmeal.getCategoryId())
                .eq(Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> list = setmealService.list(lqw);
        return R.success(list);
    }

}
