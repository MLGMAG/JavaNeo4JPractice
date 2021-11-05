package com.mlgmag.javaNeo4J.dto;

import com.mlgmag.javaNeo4J.entity.employer.Employer;
import com.mlgmag.javaNeo4J.entity.project.Project;
import lombok.Data;

@Data
public class ProjectEmployerDto {
    private Employer employer;
    private Project project;
}
