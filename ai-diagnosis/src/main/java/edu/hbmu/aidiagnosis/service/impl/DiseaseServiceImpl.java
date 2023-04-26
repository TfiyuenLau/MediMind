package edu.hbmu.aidiagnosis.service.impl;

import edu.hbmu.aidiagnosis.dao.DiseaseRepository;
import edu.hbmu.aidiagnosis.domain.node.Disease;
import edu.hbmu.aidiagnosis.service.IDiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DiseaseServiceImpl implements IDiseaseService {

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Override
    public Disease findDiseaseById(Long id) {
        return diseaseRepository.findDiseaseById(id);
    }

    @Override
    public List<Disease> findDiseaseBySymptom(String symptom) {

        return diseaseRepository.findDiseasesBySymptom(symptom);
    }

    /**
     * 根据症状集合获取病症集合，按疾病出现次数排序
     *
     * @param symptomSet
     * @return
     */
    @Override
    public List<Map.Entry<Disease, Integer>> findDiseaseBySymptoms(Set<String> symptomSet) {
        // 遍历所有病症可能的疾病，统计症状出现的次数
        Map<Disease, Integer> diseaseMap = new HashMap<>();
        for (String symptom : symptomSet) {
            for (Disease disease : findDiseaseBySymptom(symptom)) {
                if (!diseaseMap.containsKey(disease)) {
                    diseaseMap.put(disease, 1);
                } else {
                    diseaseMap.put(disease, diseaseMap.get(disease) + 1);
                }
            }
        }

        // 转换为MapEntry List并排序
        List<Map.Entry<Disease, Integer>> diseaseList = new ArrayList<Map.Entry<Disease, Integer>>(diseaseMap.entrySet());
        diseaseList.sort(new Comparator<Map.Entry<Disease, Integer>>() {
            @Override
            public int compare(Map.Entry<Disease, Integer> o1, Map.Entry<Disease, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        return diseaseList;
    }

    @Override
    @Transactional(value = "neo4jTransaction")// 添加事务
    public Disease saveDisease(Disease disease) {
        return diseaseRepository.save(disease);
    }

    @Override
    @Transactional(value = "neo4jTransaction")
    public void deleteDisease(Disease disease) {
        diseaseRepository.delete(disease);
    }

}
