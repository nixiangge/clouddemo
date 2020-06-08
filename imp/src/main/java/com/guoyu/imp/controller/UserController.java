package com.guoyu.imp.controller;


import com.guoyu.imp.dao.RootDao;
import com.guoyu.imp.entity.Root;
import com.guoyu.imp.mapper.RootMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RootDao rootDao;

    @Autowired
    private RootMapper rootMapper;

    @RequestMapping(value = "/jpa/{id}",method = RequestMethod.GET)
    public Optional<Root> getJpaOne(@PathVariable String id){
        return rootDao.findById(id);
    }
    @RequestMapping(value = "/mybatis/{id}",method = RequestMethod.GET)
    public Root getMyBatisOne(@PathVariable String id){

        return rootMapper.findOne(id);
    }
    @RequestMapping(value = "/one/{id}",method = RequestMethod.GET)
    public Root showOne(@PathVariable String id){
        return rootDao.findOne(id);
    }
}
