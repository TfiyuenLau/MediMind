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
    public Disease findDiseaseByName(String name) {
        return diseaseRepository.findDiseaseByName(name);
    }

    @Override
    public List<Disease> findDiseaseByNameContaining(String name) {

        return diseaseRepository.findDiseasesByNameContaining(name);
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

    @Transactional(value = "neo4jTransaction")
    @Override
    public void deleteDiseaseById(Long id) {
        diseaseRepository.deleteById(id);
    }

    /**
     * 按照type删除Disease实体节点的一个关系
     *
     * @param diseaseId 疾病ID
     * @param otherId   另一个实体节点的ID
     * @param type      待删除的关系类型
     */
    @Transactional(value = "neo4jTransaction")
    @Override
    public void deleteDiseaseRelationship(Long diseaseId, Long otherId, String type) {
        if (Objects.equals(type, "has_symptom")) {
            diseaseRepository.deleteSymptomRelation(diseaseId, otherId);
        } else if (Objects.equals(type, "need_check")) {
            diseaseRepository.deleteNeedCheckRelationship(diseaseId, otherId);
        } else if (Objects.equals(type, "acompany_with")) {
            diseaseRepository.deleteAccompanyRelation(diseaseId, otherId);
        } else if (Objects.equals(type, "common_drug")) {
            diseaseRepository.deleteCommonDrugRelation(diseaseId, otherId);
        } else if (Objects.equals(type, "recommand_drug")) {
            diseaseRepository.deleteRecommendDrugRelation(diseaseId, otherId);
        } else if (Objects.equals(type, "recommand_eat")) {
            diseaseRepository.deleteRecommendEatRelation(diseaseId, otherId);
        } else if (Objects.equals(type, "do_eat")) {
            diseaseRepository.deleteDoEatRelations(diseaseId, otherId);
        } else if (Objects.equals(type, "no_eat")) {
            diseaseRepository.deleteNoEatRelation(diseaseId, otherId);
        } else {
            throw new IllegalArgumentException("type error!" + type + " is not correct type");
        }
    }

}
