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
    //????????????
    void testCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("xauat");
        client.indices().create(request, RequestOptions.DEFAULT);
    }

	@Test
    //????????????????????????
	void isExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("xauat");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Test
    //????????????
    void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("xauat");
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    @Test
    //????????????
    void createDocument() throws IOException {
        //????????????
        IndexRequest request = new IndexRequest("xauat");
        //????????????
        Student student = new Student("??????",
                40, "????????????", Arrays.asList("??????", "??????"));
        //????????????
        request.id("6");
        request.timeout(TimeValue.timeValueMillis(1));
        //???????????????json??????????????????
        request.source(JSON.toJSONString(student), XContentType.JSON);
        //????????????
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);

        //??????????????????
        System.out.println(indexResponse.toString());
        //????????????
        System.out.println(indexResponse.status());
    }

    @Test
    //????????????
    void getDocument() throws IOException {
        GetRequest request = new GetRequest("xauat", "6");

        //??????????????????
        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);

        GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
        //??????????????????
        System.out.println(getResponse);
        //??????????????????
        System.out.println(getResponse.getSourceAsString());
    }

    @Test
    //????????????
    void updateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("xauat", "6");

        Student student = new Student("??????",
                40, "????????????", Arrays.asList("??????", "??????"));

        request.doc(JSON.toJSONString(student), XContentType.JSON);
        UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
        System.out.println(update.status());
    }

    @Test
    //????????????
    void DeleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("xauat", "3");
        DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.status());
    }

    @Test
    //????????????
    void  addBatch() throws IOException {
        List<Student> students = new ArrayList<>();
        //????????????
        BulkRequest bulkRequest = new BulkRequest();
        students.add(new Student("??????",
                40, "????????????", Arrays.asList("??????", "??????")));
        students.add(new Student("??????",
                40, "????????????", Arrays.asList("??????", "??????")));
        students.add(new Student("??????",
                40, "????????????", Arrays.asList("??????", "??????")));
        students.add(new Student("??????",
                40, "????????????", Arrays.asList("??????", "??????")));

        for (int i = 0; i < students.size(); i++) {
            bulkRequest.add(new IndexRequest("xauat")
                    .id(i + 6 + "")
                    .source(JSON.toJSONString(students.get(i)), XContentType.JSON));
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println(bulk.status());
        }
    }

    @Test
    //??????
    void SearchDocument() throws IOException {
        SearchRequest request = new SearchRequest();
        //??????????????????
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //??????????????????
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("name", "dunk");
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(TimeValue.timeValueMillis(2000));
        request.source(searchSourceBuilder);

        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        //???????????????
        SearchHits hits = search.getHits();
        System.out.println(JSON.toJSONString(hits));
        //??????????????????????????????
        for (SearchHit hit : hits.getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
        
    }



}
