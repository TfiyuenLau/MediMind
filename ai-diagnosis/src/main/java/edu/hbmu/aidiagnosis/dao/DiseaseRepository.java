package edu.hbmu.aidiagnosis.dao;

import edu.hbmu.aidiagnosis.domain.node.Disease;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends Neo4jRepository<Disease, Long> {

    /**
     * 根据id查找疾病对象
     *
     * @param id
     * @return
     */
    Disease findDiseaseById(Long id);

    /**
     * 精确查询获取疾病对象
     *
     * @param name
     * @return
     */
    Disease findDiseaseByName(String name);

    /**
     * 模糊查询疾病对象
     *
     * @param name
     * @return
     */
    List<Disease> findDiseasesByNameContaining(String name);

    /**
     * 根据病症查疾病
     *
     * @param symptom
     * @return
     */
    @Query("MATCH (m:Disease)-[r:has_symptom]->(n:Symptom) where n.name = $symptom return m, n")
    List<Disease> findDiseasesBySymptom(String symptom);

    /**
     * 通过疾病id与病症id删除其中的并发症关系
     *
     * @param diseaseId
     * @param symptomId
     */
    @Modifying
    @Query("MATCH (n:Disease)-[r:has_symptom]->(s:Symptom) WHERE id(n) = $diseaseId AND id(s) = $symptomId DELETE r")
    void deleteSymptomRelation(@Param("diseaseId") Long diseaseId, @Param("symptomId") Long symptomId);

    /**
     * 删除疾病需要的检查关系
     *
     * @param diseaseId
     * @param checkId
     */
    @Modifying
    @Query("MATCH (d:Disease)-[r:need_check]->(c:Check) WHERE ID(d) = $diseaseId AND ID(c) = $checkId DELETE r")
    void deleteNeedCheckRelationship(@Param("diseaseId") Long diseaseId, @Param("checkId") Long checkId);

    /**
     * 删除疾病间的并发症关系
     *
     * @param diseaseId
     * @param accompanyDiseaseId
     */
    @Modifying
    @Query("MATCH (d:Disease)-[r:acompany_with]->(d1:Disease) WHERE id(d) = $diseaseId AND id(d1) = $accompanyDiseaseId DELETE r")
    void deleteAccompanyRelation(@Param("diseaseId") Long diseaseId, @Param("accompanyDiseaseId") Long accompanyDiseaseId);

    /**
     * 删除常用药物关系
     *
     * @param diseaseId
     * @param commonDrugId
     */
    @Modifying
    @Query("MATCH (d:Disease)-[r:common_drug]->(d1:Disease) WHERE id(d) = $diseaseId AND id(d1) = $commonDrugId DELETE r")
    void deleteCommonDrugRelation(@Param("diseaseId") Long diseaseId, @Param("commonDrugId") Long commonDrugId);

    /**
     * 删除推荐药物关系
     *
     * @param diseaseId
     * @param recommendDrugId
     */
    @Modifying
    @Query("MATCH (d:Disease)-[r:recommand_drug]->(dr:Drug) WHERE id(d) = $diseaseId AND id(dr) = $recommendDrugId DELETE r")
    void deleteRecommendDrugRelation(@Param("diseaseId") Long diseaseId, @Param("recommendDrugId") Long recommendDrugId);

    /**
     * 删除推荐食物关系
     *
     * @param diseaseId
     * @param recommendEatId
     */
    @Modifying
    @Query("MATCH (d:Disease)-[r:recommand_eat]->(f:Food) WHERE id(d) = $diseaseId AND id(f) = $recommendEatId DELETE r")
    void deleteRecommendEatRelation(@Param("diseaseId") Long diseaseId, @Param("recommendEatId") Long recommendEatId);

    /**
     * 删除宜食用食物关系
     *
     * @param diseaseId
     * @param foodId
     */
    @Modifying
    @Query("MATCH (d:Disease)-[r:do_eat]->(f:Food) WHERE id(d) = $diseaseId AND id(f) = $foodId DELETE r")
    void deleteDoEatRelations(@Param("diseaseId") Long diseaseId, @Param("foodId") Long foodId);

    /**
     * 删除忌食食物关系
     *
     * @param diseaseId
     * @param foodId
     */
    @Query("MATCH (d:Disease)-[r:no_eat]->(f:Food) WHERE ID(d) = $diseaseId AND ID(f) = $foodId DELETE r")
    void deleteNoEatRelation(@Param("diseaseId") Long diseaseId, @Param("foodId") Long foodId);

}
