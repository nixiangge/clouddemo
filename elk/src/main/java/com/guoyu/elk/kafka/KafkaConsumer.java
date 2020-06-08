package com.guoyu.elk.kafka;

import com.alibaba.fastjson.JSON;
import com.guoyu.elk.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics = "demo")
    public void listen (ConsumerRecord<?, ?> record) throws Exception {
        String ss =record.value().toString();
        User user=JSON.parseObject(ss, User.class);
        System.out.println(user.getName());
        System.out.printf("topic = %s, offset = %d, value = %s \n", record.topic(), record.offset(), record.value());
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("----------------- record =" + record);
            log.info("------------------ message =" + message);
        }
    }
}
