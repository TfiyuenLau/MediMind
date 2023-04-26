package edu.hbmu.aidiagnosis.domain.relationship;

import edu.hbmu.aidiagnosis.domain.node.Drug;
import edu.hbmu.aidiagnosis.domain.node.Producer;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * 药物——在售药物
 */
@Data
@RelationshipEntity(value = "drugs_of")
public class DrugsOfProducer {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Drug drug;

    @EndNode
    private Producer producer;

}
