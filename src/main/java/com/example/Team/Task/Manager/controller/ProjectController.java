package com.example.Team.Task.Manager.controller;

import com.example.Team.Task.Manager.dtoProject.*;
import com.example.Team.Task.Manager.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;


    @PostMapping("/createProject")
    public ProjectResponse createProject(@RequestBody ProjectRequest projectRequest) {
        return projectService.createProject(projectRequest);
    }

    @GetMapping("/myProjects")
    public ResponseEntity<List<ProjectResponse>> getUserProjects() {
        List<ProjectResponse> projects = projectService.allUserProject();
        return ResponseEntity.ok(projects);
    }
    @DeleteMapping("/projects/{projectId}/deleteProject")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
    }

    @PutMapping("/projects/{projectId}/updateName")
    public void nameProjectUpdate(@PathVariable Long projectId, @RequestBody ProjectRequestUpdate dto) {
        projectService.nameProjectUpdate(projectId, dto);
    }

    @PostMapping("/projects/{projectId}/addUser")
    public void addUserToProject(@PathVariable Long projectId, @RequestBody AddUserInProject dto) {

        projectService.addUserToProject(projectId, dto);
    }

    @DeleteMapping("/projects/{projectId}/deleteUserInProject")
    public void deleteUser(@PathVariable Long projectId, @RequestBody DeleteUser dto) {
        projectService.deleteUser(projectId, dto);
    }


}
