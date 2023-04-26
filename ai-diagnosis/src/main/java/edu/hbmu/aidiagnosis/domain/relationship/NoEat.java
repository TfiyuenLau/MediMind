package edu.hbmu.aidiagnosis.domain.relationship;

import edu.hbmu.aidiagnosis.domain.node.Disease;
import edu.hbmu.aidiagnosis.domain.node.Food;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * 疾病——忌吃食物
 */
@Data
@RelationshipEntity(value = "no_eat")
public class NoEat {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Disease disease;

    @EndNode
    private Food food;

}
