package com.example.Team.Task.Manager.dtoProject;

import com.example.Team.Task.Manager.entity.Role;
import lombok.Data;

@Data
public class AddUserInProject {
    private String nameProject;
    private String username;
    private Role role;
}
