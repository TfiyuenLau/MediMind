package edu.hbmu.aidiagnosis.domain.node;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NodeEntity(label = "Disease")
public class Disease {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "name")// 疾病名称
    private String name;

    @Property(name = "desc")// 疾病简介
    private String description;

    @Property(name = "cause")// 疾病病因
    private String cause;

    @Property(name = "prevent")// 预防措施
    private String prevent;

    @Property(name = "cure_lasttime")// 治疗周期
    private String cureLastTime;

    @Property(name = "cure_way")//治疗方式
    private Set<String> cureWay;

    @Property(name = "cured_prob")// 治愈概率
    private String curedProb;

    @Property(name = "easy_get")// 疾病易感人群
    private String easyGet;

    // 疾病——并发症（疾病）
    @Relationship(value = "acompany_with")
    private Set<Disease> accompanyDiseaseSet;

    // 疾病——常用药物
    @Relationship(value = "common_drug")
    private Set<Drug> commonDrugSet;

    // 疾病——病症
    @Relationship(value = "has_symptom")
    private Set<Symptom> symptomSet;

    // 疾病——所需检查
    @Relationship(value = "need_check")
    private Set<Check> checkSet;

    // 疾病——推荐药品
    @Relationship(value = "recommand_drug")
    private Set<Drug> recommendDrugSet;

    // 疾病——推荐食物
    @Relationship(value = "recommand_eat")
    private Set<Food> recommendEatSet;

    // 疾病——宜吃食物
    @Relationship(value = "do_eat")
    private Set<Food> doEatSet;

    // 疾病——忌吃食物
    @Relationship(value = "no_eat")
    private Set<Food> noEatSet;

    public Disease() {
        this.accompanyDiseaseSet = new HashSet<>();
        this.commonDrugSet = new HashSet<>();
        this.symptomSet = new HashSet<>();
        this.checkSet = new HashSet<>();
        this.recommendDrugSet = new HashSet<>();
        this.recommendEatSet = new HashSet<>();
        this.doEatSet = new HashSet<>();
        this.noEatSet = new HashSet<>();
    }

}

