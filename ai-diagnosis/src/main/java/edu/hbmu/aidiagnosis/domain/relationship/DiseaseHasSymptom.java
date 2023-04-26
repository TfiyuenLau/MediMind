package edu.hbmu.aidiagnosis.domain.relationship;

import edu.hbmu.aidiagnosis.domain.node.Disease;
import edu.hbmu.aidiagnosis.domain.node.Symptom;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * 疾病——症状
 */
@Data
@RelationshipEntity(value = "has_symptom")
public class DiseaseHasSymptom {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Disease disease;

    @EndNode
    private Symptom symptom;

}
