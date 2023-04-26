package edu.hbmu.aidiagnosis.domain.relationship;

import edu.hbmu.aidiagnosis.domain.node.Department;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * 科室——属于
 */
@Data
@RelationshipEntity(value = "belongs_to")
public class DepartmentBelongsTo {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Department childDepartment;

    @EndNode
    private Department department;

}
