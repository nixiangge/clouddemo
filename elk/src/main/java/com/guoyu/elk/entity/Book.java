package com.guoyu.elk.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "guoyu",type = "_doc")
public class Book {
    @Id
    @Field(type = FieldType.Text)
    private String id;
    @Field(analyzer = "ik_smart",searchAnalyzer="ik_smart",type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Double)
    private Double price;
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer="ik_smart")
    private String des;
    @Field(type = FieldType.Date)
    private Date date;


}
