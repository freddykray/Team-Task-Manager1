package com.example.Team.Task.Manager.service;

import com.example.Team.Task.Manager.entity.Project;
import com.example.Team.Task.Manager.entity.Task;
import com.example.Team.Task.Manager.entity.User;
import com.example.Team.Task.Manager.entity.UserProject;

import java.util.List;
import java.util.Optional;

public interface EntityFinder {

    User getUserByName(String username);

    Project getProjectByName(String projectName);

    boolean isUserOwner(Project project);

    boolean isUserOwnerAndAdmin(String projectName);

    List<Project> findProjectsByUser(User user);

    Optional<UserProject> userInProject(User user, Project project);

    Task getTaskInProject(Project project, String dto);

}
