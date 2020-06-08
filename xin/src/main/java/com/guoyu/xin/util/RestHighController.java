package com.guoyu.xin.util;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest")
@Slf4j
public class RestHighController {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @RequestMapping("create")
    public void createIndex(String indexName, String settings) {
        try{
            CreateIndexRequest request = new CreateIndexRequest("admin");
            request.settings(Settings.builder().put("index.number_of_shards",3)
                    .put("index.number_of_replicas", 2) // 副本数
            );
            Map<String, Object> jsonMap = new HashMap<>();
            Map<String, Object> properties = new HashMap<>();
            Map<String, Object> name = new HashMap<>();
            name .put("type", "text");
            name .put("analyzer", "ik_max_word");
            properties.put("name", name);
            Map<String, Object> age = new HashMap<>();
            age .put("type", "Integer");
            properties.put("age", age);
            Map<String, Object> content = new HashMap<>();
            content .put("type", "text");
            content .put("analyzer", "ik_smart");
            properties.put("content", content);
            jsonMap.put("properties", properties);
            request.mapping(jsonMap);
            restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            log.info("索引创建成功");
        }catch (Exception e){
            log.error("索引创建失败:{}", e);
        }
    }
}
