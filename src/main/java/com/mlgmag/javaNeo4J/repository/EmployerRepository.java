package com.mlgmag.javaNeo4J.repository;

import com.mlgmag.javaNeo4J.entity.employer.Employer;
import com.mlgmag.javaNeo4J.entity.employer.EmployerDataLayer;
import com.mlgmag.javaNeo4J.entity.employer.EmployerPropertiesLayer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface EmployerRepository extends Neo4jRepository<Employer, String> {

    @Query("CREATE (e:Employer {name: :#{#employer.name}, age: :#{#employer.age}}) RETURN e")
    EmployerPropertiesLayer createNewEmployer(@Param("employer") Employer employer);

    @Query("MATCH (e:Employer {name: :#{#employer.name}}) SET e.age=:#{#employer.age} RETURN e")
    EmployerPropertiesLayer updateEmployer(@Param("employer") Employer employer);

    @Query("MATCH (e:Employer {name: :#{#employer.name}}) DELETE (e)")
    void deleteEmployer(@Param("employer") Employer employer);

    @Query("MATCH (e:Employer) WHERE(e.name = :#{#employer.name}) RETURN count(e) > 0")
    boolean isExists(@Param("employer") Employer employer);

    @Query("MATCH (e1:Employer {name: :#{#employer1.name}}), (e2:Employer {name: :#{#employer2.name}}) CREATE ((e1)-[f:friend{sourceEmployerName: :#{#employer1.name}, targetEmployer: :#{#employer2.name}}]->(e2))")
    void makeFriends(@Param("employer1") Employer employer1, @Param("employer2") Employer employer2);

    @Query("MATCH (e1:Employer {name: :#{#employer1.name}})-[f:friend]->(e2:Employer {name: :#{#employer2.name}}) DELETE f")
    void unFriend(@Param("employer1") Employer employer1, @Param("employer2") Employer employer2);

    @Query("MATCH (e1:Employer {name: :#{#employer1.name}}), (e2:Employer {name: :#{#employer2.name}}), friends = ((e1)-[f:friend]->(e2)) RETURN COUNT (friends) > 0")
    boolean isFriends(@Param("employer1") Employer employer1, @Param("employer2") Employer employer2);

    @Query("MATCH (e:Employer)-[r]->() WHERE e.name= :#{#employer.name} RETURN COUNT(r) > 0")
    boolean containsRelations(@Param("employer") Employer employer);

    @Query("MATCH (e:Employer)-[r]->() WHERE e.name= :#{#employer.name} DELETE r")
    void deleteAllRelations(@Param("employer") Employer employer);

    EmployerDataLayer getEmployerByName(String employerName);
}
