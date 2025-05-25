package com.example.Team.Task.Manager.dtoTask;

import com.example.Team.Task.Manager.entity.TaskStatus;
import lombok.Data;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private String createdAt;
    private String assignee;

}
