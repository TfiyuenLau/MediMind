package edu.hbmu.aidiagnosis.service;

import edu.hbmu.aidiagnosis.domain.node.Disease;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IDiseaseService {

    Disease findDiseaseById(Long id);

    List<Disease> findDiseaseBySymptom(String symptom);

    List<Map.Entry<Disease, Integer>> findDiseaseBySymptoms(Set<String> symptomSet);

    Disease saveDisease(Disease disease);

    void deleteDisease(Disease disease);
}
