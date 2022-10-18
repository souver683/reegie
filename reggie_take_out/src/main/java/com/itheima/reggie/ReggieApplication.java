package com.itheima.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.*;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description reggie启动类
 * @data 2022/9/30 19 : 56 : 16
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("SprinBoot项目启动~~~");
        Queue<Integer> queue=new LinkedList<>();
        queue.isEmpty();
    }
}
