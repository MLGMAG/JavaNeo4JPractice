package com.mlgmag.javaNeo4J.service;

import com.mlgmag.javaNeo4J.dto.ProjectEmployerDto;
import com.mlgmag.javaNeo4J.entity.project.Project;
import com.mlgmag.javaNeo4J.entity.project.ProjectDataLayer;
import com.mlgmag.javaNeo4J.entity.project.ProjectPropertiesLayer;
import com.mlgmag.javaNeo4J.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployerService employerService;

    public ProjectService(ProjectRepository projectRepository, EmployerService employerService) {
        this.projectRepository = projectRepository;
        this.employerService = employerService;
    }

    public List<ProjectPropertiesLayer> createProjects(List<Project> projects) {
        return projects.stream()
                .filter(this::isNotExists)
                .map(projectRepository::createNewProject)
                .collect(Collectors.toList());
    }

    public boolean isExists(Project project) {
        return projectRepository.isProjectExists(project);
    }

    public boolean isNotExists(Project project) {
        return !isExists(project);
    }

    public void deleteProjects(List<Project> projects) {
        List<Project> existingProjects = projects.stream().filter(this::isExists).collect(Collectors.toList());
        existingProjects.forEach(projectRepository::deleteAllRelations);
        existingProjects.forEach(projectRepository::deleteProjectByTitle);
    }

    public void addProjectEmployer(List<ProjectEmployerDto> projectEmployers) {
        projectEmployers.stream()
                .filter(projectEmployer -> employerService.isExists(projectEmployer.getEmployer()))
                .filter(projectEmployer -> isExists(projectEmployer.getProject()))
                .filter(this::isNotAlreadyEmployed)
                .forEach(projectEmployer -> projectRepository.addProjectEmployer(projectEmployer.getProject(), projectEmployer.getEmployer()));
    }

    public boolean isAlreadyEmployed(ProjectEmployerDto projectEmployer) {
        return projectRepository.isAlreadyEmployed(projectEmployer.getProject(), projectEmployer.getEmployer());
    }

    public boolean isNotAlreadyEmployed(ProjectEmployerDto projectEmployer) {
        return !isAlreadyEmployed(projectEmployer);
    }

    public ProjectDataLayer getProjectByTitle(String title) {
        return projectRepository.getProjectByTitle(title);
    }
}
