package com.serice.sericefeign.utils;

import com.serice.sericefeign.exception.NoMappingParamString;
import com.serice.sericefeign.feign.Hifeign;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SchedualServiceHiHystric implements Hifeign {
    @Override
    public Map<String, String> getDictValues (String code) throws RuntimeException {
        throw new RuntimeException("请求服务超时请联系管理员！");
        //return null;
    }

    @Override
    public String getString() {
        return "请求服务超时请联系管理员";

    }

}
