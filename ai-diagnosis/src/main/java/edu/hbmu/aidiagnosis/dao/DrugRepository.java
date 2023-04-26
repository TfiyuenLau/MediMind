package edu.hbmu.aidiagnosis.dao;

import edu.hbmu.aidiagnosis.domain.node.Drug;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrugRepository extends Neo4jRepository<Drug, Long> {

    /**
     * 通过疾病名称查询常用药物
     *
     * @param diseaseName 疾病名称
     * @return
     */
    @Query("MATCH (m:Disease)-[r:common_drug]->(n:Drug) where m.name = $diseaseName return n;")
    List<Drug> findCommonDrugByDisease(String diseaseName);

    /**
     * 通过疾病名称查询推荐药物
     *
     * @param diseaseName
     * @return
     */
    @Query("MATCH (m:Disease)-[r:recommand_drug]->(n:Drug) where m.name = $diseaseName return n;")
    List<Drug> findRecommendDrugByDisease(String diseaseName);

}
