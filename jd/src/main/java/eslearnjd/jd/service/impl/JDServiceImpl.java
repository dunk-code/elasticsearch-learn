package eslearnjd.jd.service.impl;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.regexp.internal.RE;
import eslearnjd.jd.pojo.Content;
import eslearnjd.jd.pojo.ParseBlog;
import eslearnjd.jd.service.JDService;
import eslearnjd.jd.utils.BlogParseUtil;
import eslearnjd.jd.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ：zsy
 * @date ：Created 2021/6/9 16:45
 * @description：实现
 */
@Service
public class JDServiceImpl implements JDService {

    @Autowired
    RestHighLevelClient client;

    @Override
    public Boolean analyticalData(String keyword) throws IOException {
        //获取解析数据
        List<Content> contents = HtmlParseUtil.parseJD(keyword);

        //判断索引是否存在
        GetIndexRequest getRequest = new GetIndexRequest("jd_goods");
        boolean exists = client.indices().exists(getRequest, RequestOptions.DEFAULT);
        if (!exists) {
            //索引不存在，创建索引
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("jd_goods");
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            if (!createIndexResponse.isAcknowledged()) return false;
        }
        //批处理请求
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i < contents.size(); i++) {
            bulkRequest.add(new IndexRequest("jd_goods")
                    .source(JSON.toJSONString(contents.get(i)), XContentType.JSON));
        }
        bulkRequest.timeout(TimeValue.timeValueMillis(10000));
        BulkResponse searchResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        return !searchResponse.hasFailures();
    }

    @Override
    public List<Map<String, Object>> search(String keyword, int pageNo, int pageSize) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        List<Map<String, Object>> list = new ArrayList<>();
        //构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //分页
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);
        //精确匹配
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("name", keyword);
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(TimeValue.timeValueMillis(10000));
        //添加高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.requireFieldMatch(true);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        //执行搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        //查询结果集
        SearchHits hits = searchResponse.getHits();
        for(SearchHit hit : hits) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            //获取原来的结果
            HighlightField name = highlightFields.get("name");
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            if (name != null) {
                Text[] fragments = name.fragments();
                String curName = "";
                for (Text fragment : fragments) {
                    curName += fragment;
                }
                sourceAsMap.put("name", curName);
            }
            name = highlightFields.get("name");
            list.add(hit.getSourceAsMap());
        }
        return list;
    }

    @Override
    public List<Content> search(String keyword) throws IOException {

        return HtmlParseUtil.parseJD(keyword);
    }

    @Override
    public Boolean analyticalBlog() throws IOException {
        List<ParseBlog> list = BlogParseUtil.parseBlog();
        //判断索引是否存存在
        GetIndexRequest getIndexRequest = new GetIndexRequest("blog_index");
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        if (!exists) {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("blog_index");
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            if (!createIndexResponse.isAcknowledged()) return false;
        }
        BulkRequest request = new BulkRequest();
        for(int i = 0; i < list.size(); i++) {
            request.add(new IndexRequest("blog_index")
                    .source(JSON.toJSONString(list.get(i)), XContentType.JSON)
                    .id(i + ""));
        }
        request.timeout(TimeValue.timeValueMillis(30000));
        BulkResponse bulkItemResponses = client.bulk(request, RequestOptions.DEFAULT);
        return !bulkItemResponses.hasFailures();
    }

    @Override
    public List<Map<String, Object>> searchBlog(String keyword) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("content", keyword);
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(TimeValue.timeValueMillis(30000));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits) {
            list.add(hit.getSourceAsMap());
        }
        return list;
    }


}
