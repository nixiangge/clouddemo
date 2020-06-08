package com.guoyu.em.controller;


import com.guoyu.em.entity.Root;
import com.guoyu.em.mapper.RootMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private RootMapper rootMapper;

    @RequestMapping(value = "/mybatis/{id}",method = RequestMethod.GET)
    public Root getMyBatisOne(@PathVariable String id){

        return rootMapper.findOne(id);
    }

}
