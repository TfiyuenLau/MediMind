package edu.hbmu.aidiagnosis.service;

import edu.hbmu.aidiagnosis.domain.node.Disease;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IDiseaseService {

    Disease findDiseaseById(Long id);

    Disease findDiseaseByName(String name);

    List<Disease> findDiseaseByNameContaining(String name);

    List<Disease> findDiseaseBySymptom(String symptom);

    List<Map.Entry<Disease, Integer>> findDiseaseBySymptoms(Set<String> symptomSet);

    Disease saveDisease(Disease disease);

    @Transactional(value = "neo4jTransaction")
    void deleteDiseaseById(Long id);

    @Transactional(value = "neo4jTransaction")
    void deleteDiseaseRelationship(Long diseaseId, Long otherId, String type);
}
