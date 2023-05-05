package edu.hbmu.aidiagnosis.domain.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.util.Set;

@Setter
@Getter
@NodeEntity(label = "Symptom")
public class Symptom {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "name")
    private String name;

    // 有该症状的相关疾病
    @JsonIgnore
    @Relationship(value = "has_symptom", direction = Relationship.INCOMING)
    private Set<Disease> diseaseSet;

}
