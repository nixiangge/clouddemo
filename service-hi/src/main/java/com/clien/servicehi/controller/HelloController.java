package com.clien.servicehi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    @Value("${spring.application.name}")
    private String host;

    @Value("${server.port}")
    private String port;

    @RequestMapping(value="/hello/{code}",method = RequestMethod.GET)
    public Map<String,String> Hello(@PathVariable("code") String code){
        Map<String,String> map = new HashMap<String,String>();
        map.put("port",port);
        if(code.split(",").length>1){
            map.put("code",code.split(",")[0]);
            map.put("host",code.split(",")[1]);
        } else {
            map.put("code",code);
            map.put("host",host);
        }
        return map;
    }
}
