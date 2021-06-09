package school.xauat.eslearnapi;

import com.alibaba.fastjson.JSON;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexAction;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import school.xauat.eslearnapi.pojo.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
class EsLearnApiApplicationTests {

    @Autowired
    RestHighLevelClient client;


    @Test
    //创建索引
    void testCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("xauat");
        client.indices().create(request, RequestOptions.DEFAULT);
    }

	@Test
    //判断是否存在索引
	void isExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("xauat");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Test
    //删除索引
    void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("xauat");
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    @Test
    //创建文档
    void createDocument() throws IOException {
        //创建请求
        IndexRequest request = new IndexRequest("xauat");
        //创建对象
        Student student = new Student("王五",
                40, "编程大佬", Arrays.asList("编程", "学习"));
        //设置请求
        request.id("6");
        request.timeout(TimeValue.timeValueMillis(1));
        //将数据转为json格式放入请求
        request.source(JSON.toJSONString(student), XContentType.JSON);
        //发送请求
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);

        //获取响应结果
        System.out.println(indexResponse.toString());
        //响应状态
        System.out.println(indexResponse.status());
    }

    @Test
    //获取文档
    void getDocument() throws IOException {
        GetRequest request = new GetRequest("xauat", "6");

        //判断是否存在
        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);

        GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
        //返回全部内容
        System.out.println(getResponse);
        //打印文档内容
        System.out.println(getResponse.getSourceAsString());
    }

    @Test
    //更新文档
    void updateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("xauat", "6");

        Student student = new Student("王五",
                40, "编程菜鸡", Arrays.asList("编程", "互啄"));

        request.doc(JSON.toJSONString(student), XContentType.JSON);
        UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
        System.out.println(update.status());
    }

    @Test
    //删除文档
    void DeleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("xauat", "3");
        DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.status());
    }

    @Test
    //批量插入
    void  addBatch() throws IOException {
        List<Student> students = new ArrayList<>();
        //创建请求
        BulkRequest bulkRequest = new BulkRequest();
        students.add(new Student("王五",
                40, "编程菜鸡", Arrays.asList("编程", "互啄")));
        students.add(new Student("王五",
                40, "编程菜鸡", Arrays.asList("编程", "互啄")));
        students.add(new Student("王五",
                40, "编程菜鸡", Arrays.asList("编程", "互啄")));
        students.add(new Student("王五",
                40, "编程菜鸡", Arrays.asList("编程", "互啄")));

        for (int i = 0; i < students.size(); i++) {
            bulkRequest.add(new IndexRequest("xauat")
                    .id(i + 6 + "")
                    .source(JSON.toJSONString(students.get(i)), XContentType.JSON));
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println(bulk.status());
        }
    }

    @Test
    //查询
    void SearchDocument() throws IOException {
        SearchRequest request = new SearchRequest();
        //构建搜素条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //构建查询条件
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("name", "dunk");
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(TimeValue.timeValueMillis(2000));
        request.source(searchSourceBuilder);

        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        //查询结果集
        SearchHits hits = search.getHits();
        System.out.println(JSON.toJSONString(hits));
        //循环打印每个查询结果
        for (SearchHit hit : hits.getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
        
    }



}
