package com.mlgmag.javaNeo4J.entity;

import com.mlgmag.javaNeo4J.entity.employer.Employer;
import com.mlgmag.javaNeo4J.entity.project.Project;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Node("Task")
@Data
public class Task {

    @Id
    private final String summery;

    @Relationship(type = "have", direction = Relationship.Direction.INCOMING)
    private Set<Employer> employers;

    @Relationship(type = "is_a_part", direction = Relationship.Direction.OUTGOING)
    private Set<Project> projects;

}
