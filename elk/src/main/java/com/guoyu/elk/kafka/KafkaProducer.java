package com.guoyu.elk.kafka;

import com.alibaba.fastjson.JSON;
import com.guoyu.elk.entity.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("kafka")
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("send/{msg}")
    public String send(@PathVariable String msg){
        User user = new User();
        user.setAge(28);
        user.setId("6");
        user.setName(msg);
        user.setSex(1);
        kafkaTemplate.send("demo", JSON.toJSONString(user));
        return "success";
    }

}
