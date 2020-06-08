package com.guoyu.elk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.guoyu.elk.entity.Book;
import com.guoyu.elk.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

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
            request.settings(Settings.builder().put("index.number_of_shards",2)
                    .put("index.number_of_replicas", 1) // 副本数
            );
            Map<String, Object> jsonMap = new HashMap<>();
            Map<String, Object> properties = new HashMap<>();
            Map<String, Object> name = new HashMap<>();
            name .put("type", "text");
            name .put("analyzer", "ik_max_word");
            properties.put("name", name);
            Map<String, Object> age = new HashMap<>();
            age .put("type", "integer");
            properties.put("age", age);
            Map<String, Object> content = new HashMap<>();
            content .put("type", "text");
            content .put("analyzer", "ik_smart");
            properties.put("content", content);
            Map<String, Object> sex = new HashMap<>();
            sex .put("type", "integer");
            properties.put("sex", sex);
            jsonMap.put("properties", properties);
            request.mapping(jsonMap);
            restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            log.info("索引创建成功");
        }catch (Exception e){
            log.error("索引创建失败:{}", e);
        }
    }

    /**
     * 添加数据
     * @param indexName
     * @return
     */
    @RequestMapping("add/{indexName}")
    public IndexResponse sourceIndex(@PathVariable String indexName) {
        try{
            String jsonString = "{" +
                    "\"name\":\"新型冠状病毒\"," +
                    "\"age\":\"1\"," +
                    "\"sex\":\"0\"" +
                    "}";
            IndexRequest request = new IndexRequest("post");
            request.index(indexName).type("_doc").id(String.valueOf(1)).source(JSON.parseObject(jsonString), XContentType.JSON);
            IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
            log.info("elastic 索引新增成功");
            return response;
        }catch (Exception e){
            log.error("索引数据变更失败:{}", e);
        }
        return null;
    }

    /**
     * 根据查询数据
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping("one/{id}")
    public Map<String, Object> getPlayer(@PathVariable String id) throws IOException {
        GetRequest request = new GetRequest(id);
        request.index("xiangge2").type("_doc").id(id);
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        return response.getSource();
    }

    /**
     * 根据id删除数据
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping("del/{id}")
    public boolean deletePlayer(@PathVariable String id) throws IOException {
        DeleteRequest request = new DeleteRequest("xiangge2", "_doc",id);
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(response));
        return false;
    }
    @RequestMapping("update")
    public boolean updatPlayer() throws IOException {
        UpdateRequest update = new UpdateRequest();
        update.index("guoyu");
        update.id("43");
        update.type("_doc");
        String idOrCode = "ctx._source.name";
        update.script(new Script(ScriptType.INLINE,
                        "painless",
                        "if("+idOrCode+"== '绝句.杜甫'){"+idOrCode+"= '绝句'}",
                        Collections.emptyMap()));
        restHighLevelClient.update(update);
        return false;
    }
    /**
     * 根据名字查询
     * @param key
     * @param value
     * @return
     * @throws IOException
     */
    @RequestMapping("key")
    public List<User> searchMatch(String key, String value) throws IOException {
        SearchRequest request = new SearchRequest("xiangge2");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(key,value));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(100);
        request.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(search));

        SearchHit[] hits = search.getHits().getHits();
        List<User> players = new LinkedList<>();
        for(SearchHit hit : hits){
            User nbaPlayer = JSONObject.parseObject(hit.getSourceAsString(), User.class);
            players.add(nbaPlayer);
        }
        return players;
    }

    /**
     * 聚合查询
     * @param name
     * @param des
     * @param price
     * @return
     * @throws IOException
     */
    @RequestMapping("/bool")
    public List<Book> bool(String name, String des, Integer price) throws IOException {
        SearchRequest request = new SearchRequest("guoyu");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(
                new BoolQueryBuilder().must(QueryBuilders.matchQuery("des", des))
                        // .should(QueryBuilders.rangeQuery("price").lt(price))
                        //模糊查询，查询name不等于输入参数的
                        .mustNot(QueryBuilders.matchQuery("name", name))
                        //范围查询过滤price小于多少的，filer查询快，不参与分值计算
                        .filter(QueryBuilders.rangeQuery("price").lt(price)));
        request.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSON(search));
        SearchHit[] hits = search.getHits().getHits();
        List<Book> players = new LinkedList<>();
        for(SearchHit hit : hits){
            Book nbaPlayer = JSONObject.parseObject(hit.getSourceAsString(), Book.class);
            players.add(nbaPlayer);
        }
        return players;
    }
}
