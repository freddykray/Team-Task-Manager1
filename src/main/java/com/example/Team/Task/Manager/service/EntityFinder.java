package com.example.Team.Task.Manager.service;

import com.example.Team.Task.Manager.entity.Project;
import com.example.Team.Task.Manager.entity.Task;
import com.example.Team.Task.Manager.entity.User;
import com.example.Team.Task.Manager.entity.UserProject;

import java.util.List;
import java.util.Optional;

public interface EntityFinder {

    User getUserByName(String username);

    Project findById(Long projectId);

    boolean isUserOwner(Long projectId);

    boolean isUserOwnerAndAdmin(Long projectId);

    List<Project> findProjectsByUser(User user);

    Optional<UserProject> userInProject(User user, Project project);

    Task getTaskByIdInProject(Long projectId, Long taskId);

}
