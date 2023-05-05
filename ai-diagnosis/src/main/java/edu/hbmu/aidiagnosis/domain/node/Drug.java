package edu.hbmu.aidiagnosis.domain.node;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

import java.util.Set;

@Data
@NodeEntity(label = "Drug")
public class Drug {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "name")
    private String name;

    // 药品——在售药品
    @Relationship(value = "drugs_of", direction = Relationship.INCOMING)
    private Set<Producer> producer;

}
