package edu.hbmu.aidiagnosis.domain.relationship;

import edu.hbmu.aidiagnosis.domain.node.Disease;
import edu.hbmu.aidiagnosis.domain.node.Food;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * 疾病——推荐食物
 */
@Data
@RelationshipEntity(value = "recommand_eat")
public class DiseaseRecommendEat {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Disease disease;

    @EndNode
    private Food food;

}
