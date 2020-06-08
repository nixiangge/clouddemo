package com.guoyu.elk.controller;


import com.guoyu.elk.dao.BookDao;
import com.guoyu.elk.dao.UserDao;
import com.guoyu.elk.entity.Book;
import com.guoyu.elk.entity.Root;
import com.guoyu.elk.entity.User;
import com.guoyu.elk.jpadao.RootDao;
import com.guoyu.elk.util.InitBook;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RootDao rootDao;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private BookDao bookDao;



    @Value("${guoyu.userName}")
    private String userName;

    //@Transactional(isolation = Isolation.REPEATABLE_READ,propagation= Propagation.REQUIRED,rollbackFor=Exception.class,timeout=100)
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Optional<User> getOne(@PathVariable String id){
       // System.out.println(1/0);
        Book book = new Book();
        return userDao.findById(id);
    }
    @RequestMapping(value = "/jpa/{id}",method = RequestMethod.GET)
    public Optional<Root> getJpaOne(@PathVariable String id){
        return rootDao.findById(id);
    }
    @RequestMapping(value = "/one/{id}",method = RequestMethod.GET)
    public Root showOne(@PathVariable String id){
        Root user = rootDao.findOne(id);
        user.setName(this.userName);
        return rootDao.findOne(id);
    }
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public void add(@RequestBody User user){
        userDao.save(user);
    }
    @RequestMapping(value = "/name/{name}",method = RequestMethod.GET)
    public  List<String> getName(@PathVariable  String name){
        QueryBuilder queryBuilder=QueryBuilders.queryStringQuery(name).field("name").analyzer("ik_smart");
        //左右模糊
        QueryBuilder queryBuilder2=QueryBuilders.moreLikeThisQuery(new String[] {"name"});//如果不指定filedName，则默认全部，常用在相似内容的推荐上
        QueryBuilder queryBuilder0 = QueryBuilders.rangeQuery("age").from(20).to(30);
        List<String> result = new LinkedList<>();
        QueryBuilder qb1 = QueryBuilders.moreLikeThisQuery(new String[]{"name"}, new String[]{name}, null);
        /*FuzzyQueryBuilder queryBuilderH = QueryBuilders.fuzzyQuery("name", name)
                .fuzziness(Fuzziness.TWO)
                .prefixLength(0)
                .maxExpansions(10);*/
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.preTags("<h1>");
        highlightBuilder.postTags("</h1>");

        SearchRequestBuilder requestBuilder = elasticsearchTemplate.getClient().prepareSearch("xiangge")
                .setTypes("_doc")
                .setQuery(queryBuilder)
                .highlighter(highlightBuilder);


        SearchResponse response = requestBuilder.get();
        SearchHits searchHits = response.getHits();
        searchHits.forEach(i->{
            Text highlightFragment = i.getHighlightFields().get("name").getFragments()[0];
           // result.add(highlightFragment.toString());
           // result.add(i.getSourceAsString());
            result.add(highlightFragment.toString());
        });

        return result;
    }
    @RequestMapping(value = "/index")
    public String addIndex(){
        //创建索引
        elasticsearchTemplate.createIndex(Book.class);
        //完成映射
        elasticsearchTemplate.putMapping(Book.class);
      /*  Book book = new Book();
        book.setDate(new Date());
        book.setId("1");
        book.setDes("这是一本书");
        book.setName("Java开发心得");
        book.setPrice(19.6);
        IndexQuery indexQuery = new IndexQueryBuilder().withId(book.getId()).withObject(Book.class).build();
        elasticsearchTemplate.index(indexQuery);*/
        return "成功";

    }
    @RequestMapping("addBooks")
    public String addBooks(){
        List<Book> list =  new  ArrayList<Book>();
        for (int i = 0; i < 40; i++) {
            Book book = new Book();
            book.setPrice(15.6+i);
            book.setId(""+(i+1));
            book.setDate(new Date());
            book.setName(InitBook.getTitem().get(i));
            book.setDes(InitBook.getCont().get(i));
            list.add(book);
        }
        bookDao.saveAll(list);
        return "OK";
    }

    /**
     * 单字符串模糊查询，默认排序。将从所有字段中查找包含传来的word分词后字符串的数据集
     * singleWord?word=浣溪沙&size=5&page=0
     * size每页长度，page页码
     * @param word
     * @param pageable
     * @return
     */
    @RequestMapping("/singleWord")
    public List<Book>  singleTitle(String word, @PageableDefault Pageable pageable) {
        //使用queryStringQuery完成单字符串查询
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.queryStringQuery(word)).withPageable(pageable).build();
        AggregatedPage<Book> books = elasticsearchTemplate.queryForPage(searchQuery, Book.class);
        books.getTotalElements();
        return elasticsearchTemplate.queryForList(searchQuery, Book.class);
    }
    /**
     * 单字段对某字符串模糊查询
     */
    @RequestMapping("/singleMatch")
    public Object singleMatch(String name, Integer userId, @PageableDefault Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("name", name)).withPageable(pageable).build();
        return elasticsearchTemplate.queryForList(searchQuery, Book.class);
    }

    /**
     * scroll（游标） 查询
     * @param name
     * @return
     */
    @RequestMapping("/scroll")
    public List<Book> getDimissionLogList(String name) {
        ArrayList<Book> resultAdminLogs = new ArrayList<>();
        NativeSearchQueryBuilder nsQueryBuilder = new NativeSearchQueryBuilder();
        nsQueryBuilder.withQuery(QueryBuilders.matchQuery("name", name));
        nsQueryBuilder.withPageable(PageRequest.of(0,1000));
        ScrolledPage<Book> scrollAdminLog = (ScrolledPage<Book>) elasticsearchTemplate.startScroll(10000, nsQueryBuilder.build(), Book.class);
        while (scrollAdminLog.hasContent()){
            for (Book ad:scrollAdminLog) {
                resultAdminLogs.add(ad);
            }
            //取下一页，scrollId在es服务器上可能会发生变化，需要用最新的;发起continueScroll请求会重新刷新快照保留时间
            scrollAdminLog = (ScrolledPage<Book>) elasticsearchTemplate.continueScroll(scrollAdminLog.getScrollId(), 10000, Book.class);

        }
        //及时清除es快照，释放资源
        elasticsearchTemplate.clearScroll(scrollAdminLog.getScrollId());
        return resultAdminLogs;
    }
    /**
     * 单字段对某短语进行匹配查询，短语分词的顺序会影响结果
     */
    @RequestMapping("/singlePhraseMatch")
    public Object singlePhraseMatch(String des, @PageableDefault Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchPhraseQuery("des", des)).withPageable(pageable).build();
        return elasticsearchTemplate.queryForList(searchQuery, Book.class);
    }
    /**
     * 多字段合并查询
     */
    @RequestMapping("/bool")
    public Object bool(String name, String des, Integer price) {
                SearchQuery searchQuery2 = new NativeSearchQueryBuilder().
                 withQuery(new BoolQueryBuilder().must(QueryBuilders.matchQuery("des", des))
               // .should(QueryBuilders.rangeQuery("price").lt(price))
                         //模糊查询，查询name不等于输入参数的
                         .mustNot(QueryBuilders.matchQuery("name", name))
                         //范围查询过滤price小于多少的，filer查询快，不参与分值计算
                .filter(QueryBuilders.rangeQuery("price").lt(price))).build();
      /*  SearchQuery searchQuery3 = new BoolQueryBuilder().must(QueryBuilders.matchQuery("des", des))
                .should(QueryBuilders.rangeQuery("price").lt(price))
                .filter(QueryBuilders.rangeQuery("price").lt(price)).build();
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("id", id))
                .should(QueryBuilders.rangeQuery("price").lt(price)).must(QueryBuilders.matchQuery("title", title))).build();*/
      return elasticsearchTemplate.queryForList(searchQuery2, Book.class);
    }
    @RequestMapping("list")
    public List<Book> findList(){
        return bookDao.getList("浣溪沙","李清照");
    }
    @RequestMapping("update")
    public boolean updatPlayer() throws IOException {
        UpdateQuery query = new UpdateQuery();
        UpdateRequest update = new UpdateRequest();
        update.script(new Script(ScriptType.INLINE,
                "painless",
                "if(ctx._source.name == '绝句'){ctx._source.name = '绝句.杜甫'}",
                Collections.emptyMap()));
        query.setUpdateRequest(update);
        query.setIndexName("guoyu");
        query.setId("43");
        query.setType("_doc");
        elasticsearchTemplate.update(query);
        return false;
    }

    /**
     * 批量修改
     * @return
     * @throws IOException
     */
    @RequestMapping("update2")
    public boolean updatPlayer2() throws IOException {
        UpdateByQueryRequestBuilder updateByQueryRequestBuilder = UpdateByQueryAction.INSTANCE.newRequestBuilder(elasticsearchTemplate.getClient());
        updateByQueryRequestBuilder.source("xiangge");
        UpdateQuery query = new UpdateQuery();
        UpdateRequest update = new UpdateRequest();
        updateByQueryRequestBuilder.script(new Script("if(ctx._source.name == '巩祥祥'){ctx._source.age = 26}"));
        updateByQueryRequestBuilder.get().getUpdated();
        return false;
    }
    @RequestMapping(value = "/token/{name}",method = RequestMethod.GET)
    public Map<String,String> getToken(@PathVariable  String name, HttpServletRequest request){
        System.out.println(request.getHeader("token"));
        Map<String,String> token = new HashMap<>();
        token.put("token","aksdhjasbcbafhfsdfsdklasauiqi我的token123");
        token.put("name","获取Token");
        return token;
    }

    public String getUserName() {
        return userName;
    }
}
