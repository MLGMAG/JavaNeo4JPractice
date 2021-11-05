package com.mlgmag.javaNeo4J.service;

import com.mlgmag.javaNeo4J.dto.ProjectEmployerDto;
import com.mlgmag.javaNeo4J.entity.employer.Employer;
import com.mlgmag.javaNeo4J.entity.employer.EmployerPropertiesLayer;
import com.mlgmag.javaNeo4J.entity.project.Project;
import com.mlgmag.javaNeo4J.entity.project.ProjectDataLayer;
import com.mlgmag.javaNeo4J.entity.project.ProjectPropertiesLayer;
import com.mlgmag.javaNeo4J.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    public List<ProjectPropertiesLayer> getAllProjects() {
        return projectRepository.getAllEmployers();
    }

    public Map<String, Map<String, Integer>> getAllDistances() {
        Map<String, Map<String, Integer>> result = new HashMap<>();

        List<ProjectPropertiesLayer> allProjects = getAllProjects();

        for (ProjectPropertiesLayer source : allProjects) {
            Map<String, Integer> projectDistances = getProjectDistances(source, allProjects);
            result.put(source.getTitle(), projectDistances);
        }

        return result;
    }

    public Map<String, Integer> getProjectDistances(ProjectPropertiesLayer project, List<ProjectPropertiesLayer> allProjecs) {
        Map<String, Integer> result = new TreeMap<>();
        Project project1 = new Project();
        project1.setTitle(project.getTitle());

        Project project2 = new Project();
        for (ProjectPropertiesLayer target : allProjecs) {
            project2.setTitle(target.getTitle());
            if (project1.getTitle().equals(project2.getTitle())) {
                continue;
            }
            int distance = projectRepository.computeShortestDistance(project1, project2).orElse(-1);
            result.put(target.getTitle(), distance);
        }

        return result;
    }
}
