package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @author light
 * @version 1.0
 * @project 瑞吉外卖
 * @description 文件的上传和下载
 * @data 2022/10/4 19 : 18 : 47
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${Origin.basePath}")
    private String filed;
    @PostMapping("/upload")
    public R<String> image(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String suffix= originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename= UUID.randomUUID() +suffix;
        File fidir=new File(filed);
        if(!fidir.exists()){
            fidir.mkdir();
        }
        try {
            file.transferTo(new File(filed+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(filename);
    }
    @GetMapping("/download")
    public  void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream=new FileInputStream(filed+name);
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len;
            byte[] bytes=new byte[1024];
            while((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

