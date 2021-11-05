package com.mlgmag.javaNeo4J.entity.project;

import com.mlgmag.javaNeo4J.entity.employer.Employer;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node("Project")
@Data
public class Project {

    @Id
    private String title;

    @Relationship(type = "works_on", direction = Relationship.Direction.INCOMING)
    private Set<Employer> employers = new HashSet<>();

}
