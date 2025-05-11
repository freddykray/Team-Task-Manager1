package com.example.Team.Task.Manager.dtoTask;

import com.example.Team.Task.Manager.entity.TaskStatus;
import lombok.Data;

@Data
public class UpdateStatus {
    private String titleTask;
    private TaskStatus status;

}
