package edu.hbmu.aidiagnosis.domain.relationship;

import edu.hbmu.aidiagnosis.domain.node.Disease;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * 疾病——并发症（疾病）
 */
@Data
@RelationshipEntity(value = "acompany_with")
public class DiseaseAccompanyWith {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Disease disease;

    @EndNode
    private Disease accompanyDisease;

}
