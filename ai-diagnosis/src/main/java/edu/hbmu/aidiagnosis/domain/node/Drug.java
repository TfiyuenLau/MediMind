package edu.hbmu.aidiagnosis.domain.node;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

@Data
@NodeEntity(label = "Drug")
public class Drug {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "name")
    private String name;

    // 药品——在售药品
    @Relationship(value = "drugs_of")
    private Producer producer;

}
