package com.itheima.reggie.filter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description controller接口
 * @data 2022/9/30 20 : 32 : 38
 */
@RequestMapping("/employee")
@RestController
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeServiceimpl;

    /**
     * 实现登陆功能
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());
        Employee epe = employeeServiceimpl.getOne(lqw);
        if(epe==null){
            return R.error("账户不存在，登陆失败！");
        }
        if(!epe.getPassword().equals(password)){
            return R.error("密码错误，登陆失败！");
        }
        if(epe.getStatus()==0){
            return R.error("账户已被禁用，请联系管理员！");
        }
        request.getSession().setAttribute("employee",epe.getId());
        return R.success(epe);
    }

    /**
     * 实现退出功能
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功！");
    }
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("483452".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        //获取当前用户登陆的id
//        //设置创始人
//        employee.setCreateUser(empId);
//        //设置更新人
//        employee.setUpdateUser(empId);

        employeeServiceimpl.save(employee);
        return R.success("新增成员成功！");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page pages=new Page(page,pageSize);
        String countId = pages.getCountId();
        System.out.println(countId);
        LambdaQueryWrapper<Employee> lqw=new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(name),Employee::getName,name);
        lqw.orderByDesc(Employee::getUpdateTime);
        employeeServiceimpl.page(pages,lqw);
        return R.success(pages);
    }
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateUser(empId);
        employee.setUpdateTime(LocalDateTime.now());
        employeeServiceimpl.updateById(employee);
        return R.success("修改成功!");
    }
    @GetMapping("/{id}")
    public R<Employee> geyById(@PathVariable Long id){
        Employee byId = employeeServiceimpl.getById(id);
        return byId!=null?R.success(byId):R.error("用户不存在");
    }
}




