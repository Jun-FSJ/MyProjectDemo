package com.joon.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot Demo项目启动类
 *
 * @author : Joon
 * @date : 2023/3/14 16:39
 * @modyified By :
 */
@SpringBootApplication
@MapperScan({"com.joon.demo*.mapper"})
public class MyProjectDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyProjectDemoApplication.class, args);
    }

}
