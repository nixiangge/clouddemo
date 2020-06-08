package com.guoyu.admin.mapper;

import com.guoyu.admin.entity.Root;

import java.util.List;

public interface RootMapper {

    Root findOne(String id);
    List<Root> findAll();
}
