package edu.hbmu.aidiagnosis.domain.node;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

@Data
@NodeEntity(label = "Department")
public class Department {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "name")
    private String name;

    // 科室——所属父级科室
    @Relationship(value = "belongs_to", direction = Relationship.OUTGOING)// 从当前类指向属性所属的类
    private Department belongsTo;

}
