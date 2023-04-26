package edu.hbmu.aidiagnosis.domain.relationship;

import edu.hbmu.aidiagnosis.domain.node.Disease;
import edu.hbmu.aidiagnosis.domain.node.Food;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * 疾病——宜吃食物
 */
@Data
@RelationshipEntity(value = "do_eat")
public class DoEat {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Disease disease;

    @EndNode
    private Food food;

}
