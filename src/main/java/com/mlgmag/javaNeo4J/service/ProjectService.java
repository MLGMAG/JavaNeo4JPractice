package com.mlgmag.javaNeo4J.service;

import com.mlgmag.javaNeo4J.dto.ProjectEmployerDto;
import com.mlgmag.javaNeo4J.entity.project.Project;
import com.mlgmag.javaNeo4J.entity.project.ProjectDataLayer;
import com.mlgmag.javaNeo4J.entity.project.ProjectPropertiesLayer;
import com.mlgmag.javaNeo4J.model.DistanceInfo;
import com.mlgmag.javaNeo4J.repository.ProjectRepository;
import com.mlgmag.javaNeo4J.utils.DistanceUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Map<String, List<DistanceInfo>> getAllDistances() {
        Map<String, List<DistanceInfo>> result = new HashMap<>();

        List<ProjectPropertiesLayer> allProjects = getAllProjects();

        for (ProjectPropertiesLayer source : allProjects) {
            List<DistanceInfo> employerDistances = getEmployerDistances(source, allProjects);
            result.put(source.getTitle(), employerDistances);
        }

        return result;
    }

    private List<DistanceInfo> getEmployerDistances(ProjectPropertiesLayer title, List<ProjectPropertiesLayer> allTitle) {
        List<DistanceInfo> result = new ArrayList<>();

        Project sourceProject = new Project();
        sourceProject.setTitle(title.getTitle());
        for (ProjectPropertiesLayer target : allTitle) {
            if (sourceProject.getTitle().equals(target.getTitle())) {
                continue;
            }

            Project targetProject = new Project();
            targetProject.setTitle(target.getTitle());
            int distance = projectRepository.computeShortestDistance(sourceProject, targetProject).orElse(-1);
            DistanceInfo distanceInfo = DistanceInfo.builder().target(target.getTitle()).distance(distance).build();
            result.add(distanceInfo);
        }

        result.sort(DistanceUtils.DISTANCE_INFO_COMPARATOR);

        return result;
    }
}
