package com.example.Team.Task.Manager.dtoTask;

import com.example.Team.Task.Manager.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {

    private String title;
    private String description;
    private TaskStatus status;
    private String assignee;
    private LocalDateTime dateTime;
}
