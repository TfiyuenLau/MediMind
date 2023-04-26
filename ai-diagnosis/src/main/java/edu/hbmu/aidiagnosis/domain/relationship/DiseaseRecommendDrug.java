package edu.hbmu.aidiagnosis.domain.relationship;

import edu.hbmu.aidiagnosis.domain.node.Disease;
import edu.hbmu.aidiagnosis.domain.node.Drug;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * 疾病——推荐药物
 */
@Data
@RelationshipEntity(value = "recommand_drug")
public class DiseaseRecommendDrug {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Disease disease;

    @EndNode
    private Drug drug;

}
