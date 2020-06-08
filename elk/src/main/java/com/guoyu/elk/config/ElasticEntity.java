package com.guoyu.elk.config;

public class ElasticEntity {
    private String id;
    private Object Data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }
}
