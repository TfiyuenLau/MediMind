package edu.hbmu.aidiagnosis.service.impl;

import edu.hbmu.aidiagnosis.dao.DrugRepository;
import edu.hbmu.aidiagnosis.domain.node.Drug;
import edu.hbmu.aidiagnosis.service.IDrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrugServiceImpl implements IDrugService {

    @Autowired
    private DrugRepository drugRepository;

    @Override
    public List<Drug> findCommonDrugByDisease(String diseaseName) {

        return drugRepository.findCommonDrugByDisease(diseaseName);
    }

    @Override
    public List<Drug> findRecommendDrugByDisease(String diseaseName) {

        return drugRepository.findRecommendDrugByDisease(diseaseName);
    }

}
