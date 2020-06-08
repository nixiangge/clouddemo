package com.guoyu.imp.controller;


import com.guoyu.imp.entity.Root;
import com.guoyu.imp.mapper.RootMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private RootMapper rootMapper;


    @RequestMapping(value = "/mybatis/{id}",method = RequestMethod.GET)
    public Root getMyBatisOne(@PathVariable String id){

        return rootMapper.findOne(id);
    }
    @RequestMapping(value = "/UserList",method = RequestMethod.GET)
    public List<Root> showAll(@RequestParam Map<String, Object> params){
        return rootMapper.findAll();
    }
}
