package edu.hbmu.aidiagnosis.service;

import edu.hbmu.aidiagnosis.domain.node.Symptom;

import java.util.List;

public interface ISymptomService {
    Symptom findSymptomById(Long id);

    Symptom findSymptomByNameContaining(String name);

    List<Symptom> findSymptomsByDisease(String diseaseName);
}
