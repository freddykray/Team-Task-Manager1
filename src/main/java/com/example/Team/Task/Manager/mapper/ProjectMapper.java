package com.example.Team.Task.Manager.mapper;


import com.example.Team.Task.Manager.dtoProject.ProjectResponse;
import com.example.Team.Task.Manager.dtoProject.ProjectRoleResponse;
import com.example.Team.Task.Manager.dtoTask.TaskResponse;
import com.example.Team.Task.Manager.entity.Project;

import com.example.Team.Task.Manager.entity.Task;
import com.example.Team.Task.Manager.entity.TaskStatus;
import com.example.Team.Task.Manager.entity.UserProject;
import org.springframework.stereotype.Component;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    private boolean isActive = true;

    public ProjectResponse createProjectDto(Project project){
        ProjectResponse createProjectDto = new ProjectResponse();
        createProjectDto.setId(project.getId());
        createProjectDto.setName(project.getName());
        createProjectDto.setOwnerUsername(project.getOwnerUsername());
        createProjectDto.setDateTime(project.getDatetime());

        List<TaskResponse> taskResponses = project.getProjectTasks().stream()
                .map(this::toTaskResponse)
                .collect(Collectors.toList());
        createProjectDto.setProjectTasks(taskResponses);

        List<ProjectRoleResponse> projectRoleResponses = project.getUserProjects().stream()
                .map(this::toProjectRoleResponse)
                .collect(Collectors.toList());
        createProjectDto.setProjectRoles(projectRoleResponses);


        return createProjectDto;
    }

    public TaskResponse toTaskResponse(Task task){
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setStatus(task.getStatus());

        Duration duration = Duration.between(LocalDateTime.now(), task.getDatetime());

        taskResponse.setCreatedAt(formatDuration(duration));
        if (isActive ==false){
            taskResponse.setStatus(TaskStatus.Просроченна);
        }

        taskResponse.setAssignee(task.getAssignee().getUsername());
        return taskResponse;
    }

    public ProjectRoleResponse toProjectRoleResponse(UserProject projectRole){
        ProjectRoleResponse response = new ProjectRoleResponse();
        response.setId(projectRole.getId());
        response.setUsername(projectRole.getUser().getUsername());
        response.setRole(projectRole.getRole()); // Используем строковое представление роли
        return response;
    }
    private String formatDuration(Duration duration) {

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.toSeconds() % 60;

        if (days == 0){
            return String.format(" Осталось %d ч. %d мин.", hours, minutes);
        } else if (hours == 0) {
            return String.format(" Осталось %d мин.", minutes);
        } else if (minutes == 0) {
            return String.format(" Осталось %d секунд", seconds);
        } else if (seconds == 0) {
            isActive = false;

        }
        return String.format(" Осталось %d дн. %d ч. %d мин.", days, hours, minutes);
    }






}
