package edu.hbmu.aidiagnosis.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class DiseaseParams {

    private Long id;

    private String name;

    private String description;

    private String cause;

    private String prevent;

    private String cureLastTime;

    private Set<String> cureWay;

    private String curedProb;

    private String easyGet;

}

