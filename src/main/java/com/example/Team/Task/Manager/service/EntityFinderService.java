package com.example.Team.Task.Manager.service;


import com.example.Team.Task.Manager.entity.Project;
import com.example.Team.Task.Manager.entity.Role;
import com.example.Team.Task.Manager.entity.Task;
import com.example.Team.Task.Manager.entity.User;
import com.example.Team.Task.Manager.entity.UserProject;
import com.example.Team.Task.Manager.repository.ProjectRepository;
import com.example.Team.Task.Manager.repository.UserProjectRepository;
import com.example.Team.Task.Manager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EntityFinderService implements EntityFinder {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserProjectRepository userProjectRepository;

    public boolean isUserOwner(Long projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       Project project = findById(projectId);
        User owner = getUserByName(authentication.getName());
        return project.getUserProjects().stream()
                .anyMatch(up -> up.getUser().equals(owner) && up.getRole() == Role.ROLE_OWNER);
    }

    public boolean isUserOwnerAndAdmin(Long projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Project project = findById(projectId);

        User user = getUserByName(authentication.getName());

        return project.getUserProjects().stream()
                .anyMatch(up -> up.getUser().equals(user) &&
                        ( up.getRole() == Role.ROLE_ADMIN || up.getRole() == Role.ROLE_OWNER));
    }

    public User getUserByName(String user){
        User user1 = userRepository.findUserByUsername(user)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с именем " + user + " не найден"));
        return user1;
    }

    @Override
    public Project findById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Проект с таким именем не найден"));
        return project;
    }

    public List<Project> findProjectsByUser(User user){
        List<Project> projects = userProjectRepository.findAllByUser(user)
                .stream()
                .map(UserProject::getProject)
                .collect(Collectors.toList());
        return projects;
    }
    public Task getTaskByIdInProject(Long projectId, Long taskId ){
        Project project = findById(projectId);
        Task task = project.getProjectTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Задача с именем " + project + " в проекте не найдена"));
        return task;
    }
    public Optional<UserProject> userInProject(User user, Project project) {
        Optional<UserProject> userProjectOptional = userProjectRepository.findByUserAndProject(user, project);
        if (userProjectOptional.isEmpty()) {
            throw new RuntimeException("Пользователь не связан с этим проектом");
        }
        return userProjectOptional;
    }
}
