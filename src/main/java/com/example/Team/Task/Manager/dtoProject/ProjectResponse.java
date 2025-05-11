package com.example.Team.Task.Manager.dtoProject;

import com.example.Team.Task.Manager.dtoTask.TaskResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectResponse {
    private Long id;
    private String name;
    private String ownerUsername;
    private LocalDateTime dateTime;
    private List<TaskResponse> projectTasks = new ArrayList<>();
    private List<ProjectRoleResponse> projectRoles = new ArrayList<>();



}
