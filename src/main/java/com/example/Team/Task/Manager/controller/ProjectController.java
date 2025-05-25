package com.example.Team.Task.Manager.controller;


import com.example.Team.Task.Manager.dtoProject.AddUserInProject;
import com.example.Team.Task.Manager.dtoProject.DeleteUser;
import com.example.Team.Task.Manager.dtoProject.ProjectRequest;
import com.example.Team.Task.Manager.dtoProject.ProjectRequestUpdate;
import com.example.Team.Task.Manager.dtoProject.ProjectResponse;
import com.example.Team.Task.Manager.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
    @DeleteMapping("/{projectId}/deleteProject")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
    }

    @PutMapping("/{projectId}/updateName")
    public void nameProjectUpdate(@PathVariable Long projectId, @RequestBody ProjectRequestUpdate dto) {
        projectService.nameProjectUpdate(projectId, dto);
    }

    @PostMapping("/{projectId}/addUser")
    public void addUserToProject(@PathVariable Long projectId, @RequestBody AddUserInProject dto) {

        projectService.addUserToProject(projectId, dto);
    }

    @DeleteMapping("/{projectId}/deleteUserInProject")
    public void deleteUser(@PathVariable Long projectId, @RequestBody DeleteUser dto) {
        projectService.deleteUser(projectId, dto);
    }


}
