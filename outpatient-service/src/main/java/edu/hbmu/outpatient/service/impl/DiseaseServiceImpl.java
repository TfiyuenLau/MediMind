package edu.hbmu.outpatient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hbmu.outpatient.domain.doc.DiseaseDoc;
import edu.hbmu.outpatient.domain.entity.Disease;
import edu.hbmu.outpatient.dao.DiseaseMapper;
import edu.hbmu.outpatient.service.IDiseaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
@Service
public class DiseaseServiceImpl extends ServiceImpl<DiseaseMapper, Disease> implements IDiseaseService {

    @Autowired
    private DiseaseMapper diseaseMapper;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Disease getDiseaseById(Long id) {
        return diseaseMapper.selectById(id);
    }

    /**
     * 通过病症名称字段查找病症对象
     *
     * @param name
     * @return
     */
    @Override
    public List<Disease> findDiseasesByName(String name) {
        LambdaQueryWrapper<Disease> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Disease::getDiseaseName, name);
        return diseaseMapper.selectList(queryWrapper);
    }

    /**
     * 拼音自动补全疾病名称
     *
     * @param prefix 补全字段
     * @return List集合
     */
    @Override
    public List<String> getDiseaseSuggestion(String prefix) {
        try {
            SearchRequest request = new SearchRequest("disease");
            request.source().suggest(new SuggestBuilder().addSuggestion("suggestions",
                    SuggestBuilders.completionSuggestion("suggestion")
                            .prefix(prefix)
                            .skipDuplicates(true)
                            .size(10)));
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            Suggest suggest = response.getSuggest();
            CompletionSuggestion completionSuggestion = suggest.getSuggestion("suggestions");
            List<String> result = new ArrayList<>();
            for (CompletionSuggestion.Entry options : completionSuggestion) {
                for (CompletionSuggestion.Entry.Option option : options) {
                    result.add(option.getText().string());
                }
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ES解析response数据
     *
     * @param response SearchResponse对象
     * @return DiseaseDoc集合列表
     * @throws JsonProcessingException 序列化异常
     */
    private List<DiseaseDoc> handleResponse(SearchResponse response) throws JsonProcessingException {
        SearchHits searchHits = response.getHits();

        //获取全部数据并封装
        ArrayList<DiseaseDoc> diseases = new ArrayList<>();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            //反序列化
            String json = hit.getSourceAsString();
            DiseaseDoc diseaseDoc = objectMapper.readValue(json, DiseaseDoc.class);
            diseases.add(diseaseDoc);
        }

        return diseases;
    }
}
