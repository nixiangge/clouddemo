package com.guoyu.imp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.guoyu.imp.mapper") //扫描的mapper
public class ImpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImpApplication.class, args);
    }

}
