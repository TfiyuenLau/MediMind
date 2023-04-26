package edu.hbmu.outpatient;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hbmu.outpatient.constants.ElasticSearchConstants;
import edu.hbmu.outpatient.dao.DiseaseMapper;
import edu.hbmu.outpatient.dao.MedicineMapper;
import edu.hbmu.outpatient.domain.doc.DiseaseDoc;
import edu.hbmu.outpatient.domain.doc.MedicineDoc;
import edu.hbmu.outpatient.domain.entity.Disease;
import edu.hbmu.outpatient.domain.entity.Medicine;
import edu.hbmu.outpatient.service.IDiseaseService;
import edu.hbmu.outpatient.service.IMedicineService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ElasticsearchTests {

    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private DiseaseMapper diseaseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private IMedicineService medicineService;

    @Autowired
    private IDiseaseService diseaseService;

    /**
     * 初始化RestClient
     */
    @BeforeEach
    void setUp() {

    }

    /**
     * 创建medicine索引库
     *
     * @throws IOException
     */
    @Test
    void createMedicineIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("medicine");
        request.source(ElasticSearchConstants.MEDICINE_MAPPING, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * 创建disease索引库
     *
     * @throws IOException
     */
    @Test
    void createDiseaseIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("disease");
        request.source(ElasticSearchConstants.DISEASE_MAPPING, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * 批量导入medicine数据至ES
     *
     * @throws IOException
     */
    @Test
    void bulkInsertMedicine() throws IOException {
        List<Medicine> medicineList = medicineMapper.selectList(null);
        BulkRequest request = new BulkRequest();
        for (Medicine medicine : medicineList) {
            MedicineDoc medicineDoc = new MedicineDoc(medicine);
            request.add(new IndexRequest("medicine")
                    .id(medicineDoc.getMedicineId().toString())
                    .source(JSON.toJSONString(medicineDoc), XContentType.JSON));
            System.out.println(medicineDoc.getMedicineDescription() + "写入中...");
        }
        client.bulk(request, RequestOptions.DEFAULT);
    }

    /**
     * 批量导入disease数据至ES
     *
     * @throws IOException
     */
    @Test
    void bulkInsertDisease() throws IOException {
        List<Disease> diseases = diseaseMapper.selectList(null);
        BulkRequest request = new BulkRequest();
        for (Disease disease : diseases) {
            DiseaseDoc diseaseDoc = new DiseaseDoc(disease);
            request.add(new IndexRequest("disease")
                    .id(diseaseDoc.getDiseaseId().toString())
                    .source(objectMapper.writeValueAsString(diseaseDoc), XContentType.JSON));
            System.out.println(diseaseDoc.getDiseaseName() + "写入中...");
        }
        client.bulk(request, RequestOptions.DEFAULT);
    }

    /**
     * 测试ES查询
     *
     * @throws IOException
     */
    @Test
    void testSearch() throws IOException {
        SearchRequest request = new SearchRequest("disease");
//        request.source().query(QueryBuilders.termQuery("diseaseName", "后发性白内障"));
        request.source().query(QueryBuilders.matchAllQuery());
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        List<String> list = handleResponse(response);
        for (String json : list) {
            DiseaseDoc medicineDoc = objectMapper.readValue(json, DiseaseDoc.class);
            System.out.println(medicineDoc.getSuggestion());
        }
    }

    /**
     * 封装方法：返回解析后的JSON格式结果
     *
     * @param response
     * @return
     */
    private List<String> handleResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到" + total + "条数据");

        ArrayList<String> result = new ArrayList<>();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            result.add(hit.getSourceAsString());// 获取文档source
        }
        return result;
    }

    @Test
    void testMedicineSuggestion() {
        List<String> medicineSuggestion = medicineService.getMedicineSuggestion("小儿");
        for (String suggestion : medicineSuggestion) {
            System.out.println(suggestion);
        }
    }

}
