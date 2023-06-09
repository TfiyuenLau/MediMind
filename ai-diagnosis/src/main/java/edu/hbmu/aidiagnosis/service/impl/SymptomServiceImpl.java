package edu.hbmu.aidiagnosis.service.impl;

import edu.hbmu.aidiagnosis.dao.SymptomRepository;
import edu.hbmu.aidiagnosis.domain.node.Symptom;
import edu.hbmu.aidiagnosis.service.ISymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SymptomServiceImpl implements ISymptomService {

    @Autowired
    private SymptomRepository symptomRepository;

    @Override
    public Symptom findSymptomById(Long id) {
        return symptomRepository.findSymptomById(id);
    }

    @Override
    public List<Symptom> findSymptomByNameContaining(String name) {

        return symptomRepository.findSymptomsByNameContaining(name);
    }

    @Override
    public List<Symptom> findSymptomsByNameContainingNoHavaSet(String name) {

        return symptomRepository.findSymptomsByNameContainingNoHavaSet(name);
    }

    @Override
    public List<Symptom> findSymptomsByDisease(String diseaseName) {

        return symptomRepository.findSymptomsByDisease(diseaseName);
    }

    @Override
    @Transactional(value = "neo4jTransaction")
    public Symptom saveSymptom(Symptom symptom) {
        return symptomRepository.save(symptom);
    }

    @Override
    @Transactional(value = "neo4jTransaction")
    public void deleteSymptom(Long id) {
        symptomRepository.deleteById(id);
    }

}
