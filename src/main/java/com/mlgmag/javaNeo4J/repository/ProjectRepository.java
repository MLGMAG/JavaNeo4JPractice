package com.mlgmag.javaNeo4J.repository;

import com.mlgmag.javaNeo4J.entity.employer.Employer;
import com.mlgmag.javaNeo4J.entity.project.Project;
import com.mlgmag.javaNeo4J.entity.project.ProjectDataLayer;
import com.mlgmag.javaNeo4J.entity.project.ProjectPropertiesLayer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends Neo4jRepository<Project, String> {

    @Query("MATCH (p: Project) WHERE p.title= :#{#project.title} RETURN (COUNT(p) > 0)")
    boolean isProjectExists(@Param("project") Project project);

    @Query("CREATE (p: Project{title: :#{#project.title}}) RETURN (p)")
    ProjectPropertiesLayer createNewProject(@Param("project") Project project);

    @Query("MATCH (p: Project) WHERE p.title= :#{#project.title} DELETE (p)")
    void deleteProjectByTitle(@Param("project") Project project);

    @Query("MATCH (p:Project)<-[r]-() WHERE p.title= :#{#project.title} DELETE (r)")
    void deleteAllRelations(@Param("project") Project project);

    @Query("MATCH (p:Project {title: :#{#project.title}}), (e: Employer {name: :#{#employer.name}}), ((e)-[w:works_on]->(p)) RETURN (COUNT(w) > 0)")
    boolean isAlreadyEmployed(@Param("project") Project project, @Param("employer") Employer employer);

    @Query("MATCH (p:Project {title: :#{#project.title}}), (e: Employer {name: :#{#employer.name}}) CREATE ((e)-[w:works_on]->(p))")
    void addProjectEmployer(@Param("project") Project project, @Param("employer") Employer employer);

    ProjectDataLayer getProjectByTitle(String title);
}
