package com.mlgmag.javaNeo4J.rest;

import com.mlgmag.javaNeo4J.dto.ProjectEmployerRequestDto;
import com.mlgmag.javaNeo4J.entity.project.Project;
import com.mlgmag.javaNeo4J.entity.project.ProjectDataLayer;
import com.mlgmag.javaNeo4J.entity.project.ProjectPropertiesLayer;
import com.mlgmag.javaNeo4J.model.DistanceInfo;
import com.mlgmag.javaNeo4J.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
public class ProjectRestController {

    private final ProjectService projectService;

    public ProjectRestController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/")
    public List<ProjectPropertiesLayer> createAllProjects(@RequestBody List<Project> projects) {
        return projectService.createProjects(projects);
    }

    @DeleteMapping("/")
    public void deleteAllProjects(@RequestBody List<Project> projects) {
        projectService.deleteProjects(projects);
    }

    @PostMapping("/employers")
    public void addProjectEmployers(@RequestBody ProjectEmployerRequestDto projectEmployerRequest) {
        projectService.addProjectEmployer(projectEmployerRequest.getProjectEmployers());
    }

    @GetMapping("/{title}")
    public ProjectDataLayer getProjectByTitle(@PathVariable("title") String projectTitle) {
        return projectService.getProjectByTitle(projectTitle);
    }

    @GetMapping("/")
    public List<ProjectPropertiesLayer> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/shorterDistance")
    public Map<String, List<DistanceInfo>> getAllDistances() {
        return projectService.getAllDistances();
    }
}
