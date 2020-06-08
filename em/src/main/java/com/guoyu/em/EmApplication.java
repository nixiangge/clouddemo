package com.guoyu.em;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.guoyu.em.mapper") //扫描的mapper
public class EmApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmApplication.class, args);
    }

}
