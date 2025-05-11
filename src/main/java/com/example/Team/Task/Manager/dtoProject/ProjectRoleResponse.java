package com.example.Team.Task.Manager.dtoProject;

import com.example.Team.Task.Manager.entity.Role;
import lombok.Data;

@Data
public class ProjectRoleResponse {
    private Long id;
    private String username; // Имя пользователя, у которого роль
    private Role role;
}
