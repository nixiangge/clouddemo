package com.guoyu.imp.mapper;

import com.guoyu.imp.entity.Root;

import java.util.List;

public interface RootMapper {

    Root findOne(String id);
    List<Root> findAll();
}
