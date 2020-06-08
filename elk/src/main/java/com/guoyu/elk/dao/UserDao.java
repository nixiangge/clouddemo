package com.guoyu.elk.dao;

import com.guoyu.elk.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User,String> {
}
