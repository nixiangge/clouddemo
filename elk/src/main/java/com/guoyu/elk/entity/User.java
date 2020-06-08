package com.guoyu.elk.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "xiangge",type = "_doc")
@Data
public class User {

    @Id
    private String id;

    private String name;

    private Integer age;

    private Integer sex;
}
