package com.guoyu.imp.dao;

import com.guoyu.imp.entity.Root;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface RootDao extends JpaRepository<Root,String> {

    @Query("select r from Root r where uuid = ?1")
    Root findOne(String uuid);
}
