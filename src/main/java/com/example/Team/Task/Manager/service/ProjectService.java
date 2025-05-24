package com.example.Team.Task.Manager.service;

import com.example.Team.Task.Manager.dtoProject.*;
import com.example.Team.Task.Manager.entity.*;
import com.example.Team.Task.Manager.mapper.ProjectMapper;
import com.example.Team.Task.Manager.repository.ProjectRepository;
import com.example.Team.Task.Manager.repository.UserProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectService {

    private final EntityFinder entityFinder;
    private final ProjectRepository projectRepository;
    private final UserProjectRepository userProjectRepository;
    private final ProjectMapper projectMapper;

    /**
     * Создание нового проекта и установка текущего пользователя владельцем.
     */
    @Transactional
    public ProjectResponse createProject(ProjectRequest dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = entityFinder.getUserByName(authentication.getName());

        // Проверка на дублирование имени проекта
        boolean nameExists = entityFinder.findProjectsByUser(user).stream()
                .anyMatch(project -> project.getName().equals(dto.getProjectName()));
        if (nameExists) {
            throw new RuntimeException("Такое имя уже занято!");
        }

        // Создание и сохранение проекта
        Project project = new Project();
        project.setName(dto.getProjectName());
        project.setOwner(user);
        project.setDatetime(LocalDateTime.now());

        Project savedProject = projectRepository.save(project);

        // Связь пользователь - проект
        UserProject userProject = new UserProject();
        userProject.setUser(user);
        userProject.setProject(project);
        userProject.setRole(Role.ROLE_OWNER);

        userProjectRepository.save(userProject);
        return projectMapper.createProjectDto(savedProject);
    }

    /**
     * Получение всех проектов текущего пользователя.
     */
    public List<ProjectResponse> allUserProject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = entityFinder.getUserByName(authentication.getName());

        List<Project> projects = entityFinder.findProjectsByUser(user);
        return projects.stream()
                .map(projectMapper::createProjectDto)
                .collect(Collectors.toList());
    }

    /**
     * Удаление проекта, если текущий пользователь — владелец.
     */
    @Transactional
    public void deleteProject(String nameProject) {
        Project project = entityFinder.getProjectByName(nameProject);

        if (!entityFinder.isUserOwner(project)) {
            throw new RuntimeException("Вы не являетесь владельцем этого проекта!");
        }

        // Удаляем все связи с пользователями
        for (UserProject userProject : new ArrayList<>(project.getUserProjects())) {
            userProject.getUser().getUserProjects().remove(userProject);
        }

        userProjectRepository.deleteAll(project.getUserProjects());
        projectRepository.delete(project);
    }

    /**
     * Обновление названия проекта.
     */
    public void nameProjectUpdate(ProjectRequestUpdate dto) {
        Project project = entityFinder.getProjectByName(dto.getProjectName());

        if (!entityFinder.isUserOwnerAndAdmin(project.getName())) {
            throw new RuntimeException("У вас недостаточно прав!");
        }

        project.setName(dto.getNewProjectName());
        projectRepository.save(project);
    }

    /**
     * Добавление пользователя в проект.
     */
    @Transactional
    public void addUserToProject(AddUserInProject dto) {
        Project project = entityFinder.getProjectByName(dto.getNameProject());
        User user = entityFinder.getUserByName(dto.getUsername());

        boolean alreadyAdded = userProjectRepository.existsByUserAndProject(user, project);
        if (alreadyAdded) {
            throw new IllegalStateException("Пользователь уже добавлен в проект");
        }

        UserProject userProject = new UserProject();
        userProject.setUser(user);
        userProject.setProject(project);
        userProject.setRole(dto.getRole());

        userProjectRepository.save(userProject);
    }

    /**
     * Удаление пользователя из проекта владельцем.
     */
    public void deleteUser(DeleteUser dto) {
        Project project = entityFinder.getProjectByName(dto.getNameProject());
        User user = entityFinder.getUserByName(dto.getUsername());

        if (!entityFinder.isUserOwner(project)) {
            throw new RuntimeException("Вы не являетесь владельцем проекта!");
        }

        Optional<UserProject> userProjectOptional = entityFinder.userInProject(user, project);
        if (userProjectOptional.isEmpty()) {
            throw new RuntimeException("Пользователь не найден в проекте!");
        }

        UserProject userProject = userProjectOptional.get();
        project.getUserProjects().remove(userProject);
        userProjectRepository.delete(userProject);
        projectRepository.save(project);
    }
}
