package com.guoyu.elk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories("com.guoyu.elk.dao")
@EnableEurekaClient
public class ElkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElkApplication.class, args);
    }

}
