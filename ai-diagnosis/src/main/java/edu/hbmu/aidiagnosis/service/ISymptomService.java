package edu.hbmu.aidiagnosis.service;

import edu.hbmu.aidiagnosis.domain.node.Symptom;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ISymptomService {
    Symptom findSymptomById(Long id);

    List<Symptom> findSymptomByNameContaining(String name);

    List<Symptom> findSymptomsByNameContainingNoHavaSet(String name);

    List<Symptom> findSymptomsByDisease(String diseaseName);

    Symptom saveSymptom(Symptom symptom);

    @Transactional(value = "neo4jTransaction")
    void deleteSymptom(Long id);
}
