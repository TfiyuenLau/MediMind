package edu.hbmu.aidiagnosis.dao;

import edu.hbmu.aidiagnosis.domain.node.Symptom;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SymptomRepository extends Neo4jRepository<Symptom, Long> {

    /**
     * 通过id查找病症
     *
     * @param id
     * @return
     */
    Symptom findSymptomById(Long id);

    /**
     * 通过名称模糊查询病症集合
     *
     * @param name
     * @return
     */
    List<Symptom> findSymptomsByNameContaining(String name);

    /**
     * 通过名称模糊查询病症集合但不封装关系节点
     *
     * @param name
     * @return
     */
    @Query("MATCH (s:Symptom) WHERE s.name =~ ('.*'+$name+'.*') return s")
    List<Symptom> findSymptomsByNameContainingNoHavaSet(@Param("name") String name);

    /**
     * 通过疾病名称查找相关病症集合
     *
     * @param diseaseName
     * @return
     */
    @Query("MATCH (m:Disease)-[r:has_symptom]->(n:Symptom) where m.name = $diseaseName return n")
    List<Symptom> findSymptomsByDisease(@Param("diseaseName") String diseaseName);

}
