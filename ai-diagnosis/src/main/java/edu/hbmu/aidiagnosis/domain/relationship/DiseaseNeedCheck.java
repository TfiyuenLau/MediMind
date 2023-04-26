package edu.hbmu.aidiagnosis.domain.relationship;

import edu.hbmu.aidiagnosis.domain.node.Check;
import edu.hbmu.aidiagnosis.domain.node.Disease;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * 疾病——所需检查
 */
@Data
@RelationshipEntity(value = "need_check")
public class DiseaseNeedCheck {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Disease disease;

    @EndNode
    private Check check;

}
