package com.serice.sericefeign.feign;

import com.serice.sericefeign.exception.NoMappingParamString;
import com.serice.sericefeign.utils.SchedualServiceHiHystric;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(value = "eureka-hi",fallback = SchedualServiceHiHystric.class)
public interface Hifeign {
    @RequestMapping(value = "cloud/hello/{code}",method = RequestMethod.GET)
    public Map<String,String> getDictValues(@PathVariable("code") String code) throws NoMappingParamString;

    @RequestMapping(value = "cloud/hello",method = RequestMethod.GET)
    public String getString() throws NoMappingParamString;
}
