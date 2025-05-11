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

    @DeleteMapping("/deleteProject")
    public void deleteProject(@RequestBody ProjectRequest dto){
        projectService.deleteProject(dto.getProjectName());
    }

    @PutMapping("/updateName")
    public void nameProjectUpdate(@RequestBody ProjectRequestUpdate dto){
        projectService.nameProjectUpdate(dto);
    }

    @PostMapping("/addUser")
    public void addUserToProject(@RequestBody AddUserInProject dto){
         projectService.addUserToProject(dto);
    }

    @DeleteMapping("/deleteUserInProject")
    public void deleteUser(@RequestBody DeleteUser dto){ projectService.deleteUser(dto);}

}
