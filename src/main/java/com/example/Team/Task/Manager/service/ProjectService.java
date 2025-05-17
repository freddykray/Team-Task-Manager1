package com.example.Team.Task.Manager.service;

import com.example.Team.Task.Manager.dtoProject.*;
import com.example.Team.Task.Manager.entity.Project;
import com.example.Team.Task.Manager.entity.Role;
import com.example.Team.Task.Manager.entity.User;
import com.example.Team.Task.Manager.entity.UserProject;
import com.example.Team.Task.Manager.mapper.ProjectMapper;
import com.example.Team.Task.Manager.repository.ProjectRepository;
import com.example.Team.Task.Manager.repository.UserProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private EntityFinderService entityFinderService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private ProjectMapper projectMapper;




    @Transactional
    public ProjectResponse createProject(ProjectRequest dto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Project project = new Project();
        User user = entityFinderService.getUserByName(authentication.getName());
        List<Project> projects = entityFinderService.findProjectsByUser(user);
        boolean nameExist = projects.stream().anyMatch(name -> name.getName().equals(dto.getProjectName()));

        if(nameExist){
            throw new RuntimeException("Такое имя уже занято!");
        }


        project.setName(dto.getProjectName());
        project.setOwner(user);
        project.setDatetime(LocalDateTime.now());

        Project saved = projectRepository.save(project);

        UserProject userProject = new UserProject();

        userProject.setUser(user);
        userProject.setProject(project);
        userProject.setRole(Role.ROLE_OWNER);

        if (userProject.getRole() == null) {
            throw new IllegalStateException("Роль не может быть пустой");
        }

        userProjectRepository.save(userProject);

        return projectMapper.createProjectDto(saved);
    }


    public List<ProjectResponse> allUserProject(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = entityFinderService.getUserByName(authentication.getName());
        List<Project> projects = entityFinderService.findProjectsByUser(user);

        return projects.stream()
                .map(projectMapper::createProjectDto) // преобразуем каждый проект в DTO
                .collect(Collectors.toList());
    }
    @Transactional
    public void deleteProject(String nameProject){
        Project project = entityFinderService.getProjectByName(nameProject);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = entityFinderService.getUserByName(authentication.getName());

        if (!entityFinderService.isUserOwner(project)){
            throw new RuntimeException("Вы не являетесь владельцем этого проекта!");
        }

        // Удаляем связи пользователей с проектом
        for (UserProject userProject : new ArrayList<>(project.getUserProjects())) {
            User u = userProject.getUser();
            u.getUserProjects().remove(userProject);  // корректно удаляем UserProject
        }

        userProjectRepository.deleteAll(project.getUserProjects());
        projectRepository.delete(project);
    }
    public void nameProjectUpdate(ProjectRequestUpdate dto){
        Project project = entityFinderService.getProjectByName(dto.getProjectName());

        if (!entityFinderService.isUserOwnerAndAdmin(project.getName())){
            throw new RuntimeException("У вас недостаточно прав!");
        }
        project.setName(dto.getNewProjectName());
        projectRepository.save(project);

    }

    @Transactional
    public void addUserToProject(AddUserInProject dto) {
        // Поиск проекта по имени
        Project project = entityFinderService.getProjectByName(dto.getNameProject());

        // Поиск пользователя по имени пользователя
        User user = entityFinderService.getUserByName(dto.getUsername());

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

    public void deleteUser(DeleteUser dto){
        Project project = entityFinderService.getProjectByName(dto.getNameProject());
        User user = entityFinderService.getUserByName(dto.getUsername());


        if (!entityFinderService.isUserOwner(project)){
            throw new RuntimeException("Вы не являетесь владельцем проекта!");
        }
        Optional<UserProject> userProjectOptional = entityFinderService.userInProject(user,project);

        UserProject userProject = userProjectOptional.get();
        project.getUserProjects().remove(userProject);
        userProjectRepository.delete(userProject);
        projectRepository.save(project);

    }




}
