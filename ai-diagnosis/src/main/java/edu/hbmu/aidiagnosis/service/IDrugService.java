package edu.hbmu.aidiagnosis.service;

import edu.hbmu.aidiagnosis.domain.node.Drug;

import java.util.List;

public interface IDrugService {
    List<Drug> findCommonDrugByDisease(String diseaseName);

    List<Drug> findRecommendDrugByDisease(String diseaseName);
}
