package com.example.Team.Task.Manager.dtoTask;

import lombok.Data;

@Data
public class UpdateNameTask {
    private String nameProject;
    private String oldNameTask;
    private String newNameTask;
}
