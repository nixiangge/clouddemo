package com.guoyu.elk.dao;

import com.guoyu.elk.entity.Book;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookDao extends ElasticsearchRepository<Book,String> {
    @Query("{\"bool\":{\"must\":[{\"match\":{\"name\": \"?0\"}},{\"match\":{\"des\": \"?1\"}}]}},\"from\": 0,\"size\": 1,\"sort\":{\"price\":{\"order\":\"asc\"}}")
    List<Book> getList(String name,String des);
}
