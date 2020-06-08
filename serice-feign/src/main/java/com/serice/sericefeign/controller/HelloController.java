package com.serice.sericefeign.controller;

import com.serice.sericefeign.exception.NoMappingParamString;
import com.serice.sericefeign.feign.Hifeign;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Hifeign hifeign;

    @RequestMapping(value="/hello/{code}",method = RequestMethod.GET)
    public Map<String,String> Hello(@PathVariable("code") String code) throws NoMappingParamString {
        Map<String,String> map = new HashMap<String,String>();
        return hifeign.getDictValues(code+","+host);
    }
    @RequestMapping(value="/hello",method = RequestMethod.GET)
    public String getString() throws NoMappingParamString {
        return hifeign.getString();
    }
}
