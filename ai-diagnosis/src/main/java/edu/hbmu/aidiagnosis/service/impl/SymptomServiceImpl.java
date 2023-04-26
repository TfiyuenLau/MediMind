package edu.hbmu.aidiagnosis.service.impl;

import edu.hbmu.aidiagnosis.dao.SymptomRepository;
import edu.hbmu.aidiagnosis.domain.node.Symptom;
import edu.hbmu.aidiagnosis.service.ISymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Symptom findSymptomByNameContaining(String name) {

        return symptomRepository.findSymptomByNameContaining(name);
    }

    @Override
    public List<Symptom> findSymptomsByDisease(String diseaseName) {

        return symptomRepository.findSymptomsByDisease(diseaseName);
    }

}
