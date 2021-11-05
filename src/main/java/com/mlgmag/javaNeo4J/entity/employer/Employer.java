package com.mlgmag.javaNeo4J.entity.employer;

import com.mlgmag.javaNeo4J.entity.project.Project;
import com.mlgmag.javaNeo4J.entity.Task;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node("Employer")
@Data
public class Employer {

    @Id
    private String name;

    @Property("age")
    private int age;

    @Relationship(type = "friend", direction = Relationship.Direction.OUTGOING)
    private Set<Employer> friends = new HashSet<>();

    @Relationship(type="works_on", direction = Relationship.Direction.OUTGOING)
    private Set<Project> projects = new HashSet<>();

    @Relationship(type = "have", direction = Relationship.Direction.OUTGOING)
    private Set<Task> tasks = new HashSet<>();

}
