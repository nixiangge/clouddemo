package com.guoyu.imp.dao;

import com.guoyu.imp.entity.Root;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RootDao extends JpaRepository<Root,String> {

    @Query("select r from Root r where uuid = ?1")
    Root findOne(String uuid);
    List<Root> findAll();
}
