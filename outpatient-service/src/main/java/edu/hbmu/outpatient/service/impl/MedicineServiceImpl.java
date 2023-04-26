package edu.hbmu.outpatient.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hbmu.outpatient.domain.doc.MedicineDoc;
import edu.hbmu.outpatient.domain.entity.Medicine;
import edu.hbmu.outpatient.dao.MedicineMapper;
import edu.hbmu.outpatient.service.IMedicineService;
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
public class MedicineServiceImpl extends ServiceImpl<MedicineMapper, Medicine> implements IMedicineService {

    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Medicine getMedicineById(Long id) {
        return medicineMapper.selectById(id);
    }

    @Override
    public IPage<Medicine> getMedicineByPage(Long page) {
        return medicineMapper.selectPage(new Page<>(page, 50), null);
    }

    /**
     * 用药物名称字段搜索药物对象
     *
     * @param medicineName 药物名称
     * @return
     */
    @Override
    public List<Medicine> findMedicineByName(String medicineName) {
        LambdaQueryWrapper<Medicine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Medicine::getMedicineName, medicineName);
        return medicineMapper.selectList(queryWrapper);
    }

    /**
     * 拼音自动补全药物名称
     *
     * @param prefix 补全字段
     * @return List集合
     */
    @Override
    public List<String> getMedicineSuggestion(String prefix) {
        try {
            // 发送自动补全请求
            SearchRequest request = new SearchRequest("medicine");
            request.source().suggest(new SuggestBuilder().addSuggestion("suggestions",
                    SuggestBuilders.completionSuggestion("suggestion")
                            .prefix(prefix)
                            .skipDuplicates(true)
                            .size(10)));
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            // 处理结果
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
     * @return MedicineDoc 集合列表
     * @throws JsonProcessingException 序列化异常
     */
    private List<MedicineDoc> handleResponse(SearchResponse response) throws JsonProcessingException {
        SearchHits searchHits = response.getHits();

        ArrayList<MedicineDoc> medicines = new ArrayList<>();
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            MedicineDoc medicine = objectMapper.readValue(json, MedicineDoc.class);
            medicines.add(medicine);
        }

        return medicines;
    }

}
