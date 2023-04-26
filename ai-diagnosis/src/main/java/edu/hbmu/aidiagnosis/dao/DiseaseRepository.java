package edu.hbmu.aidiagnosis.dao;

import edu.hbmu.aidiagnosis.domain.node.Disease;
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
     * 根据病症查疾病
     *
     * @param symptom
     * @return
     */
    @Query("MATCH (m:Disease)-[r:has_symptom]->(n:Symptom) where n.name = $symptom return m, n")
    List<Disease> findDiseasesBySymptom(String symptom);

}
