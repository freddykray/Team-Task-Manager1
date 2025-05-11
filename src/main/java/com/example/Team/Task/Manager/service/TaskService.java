package com.example.Team.Task.Manager.service;

import com.example.Team.Task.Manager.dtoTask.*;
import com.example.Team.Task.Manager.entity.Project;
import com.example.Team.Task.Manager.entity.Task;
import com.example.Team.Task.Manager.entity.User;
import com.example.Team.Task.Manager.entity.UserProject;
import com.example.Team.Task.Manager.repository.ProjectRepository;
import com.example.Team.Task.Manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service

public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EntityFinderService entityFinderService;

    public Task createTask(TaskRequest dto){
        if(!entityFinderService.isUserOwnerAndAdmin(dto.getNameProject())){
            throw new RuntimeException("У вас недостаточно прав!");
        }
        Task task = new Task();

        Project project = entityFinderService.getProjectByName(dto.getNameProject());

        List<Task> tasks = taskRepository.findAllByProject(project);

        boolean existTaskName = tasks.stream().anyMatch(name -> name.getTitle().equals(dto.getTitle()));
        User assignee = entityFinderService.getUserByName(dto.getAssignee());

        Optional<UserProject> userProjectOptional = entityFinderService.userInProject(assignee,project);

        if (existTaskName){
            throw new RuntimeException("Такое имя задания уже занято!");
        }
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setProject(project);
        task.setDatetime(LocalDateTime.now());
        task.setAssignee(assignee);
        Task savedTask = taskRepository.save(task);
        project.getProjectTasks().add(savedTask);
        projectRepository.save(project);

       return savedTask;
    }
    public void deleteTask(DeleteTaskDTO dto){
        if(!entityFinderService.isUserOwnerAndAdmin(dto.getNameProject())){
            throw new RuntimeException("У вас недостаточно прав!");
        }

        Project project = entityFinderService.getProjectByName(dto.getNameProject());

        Task deleteTask = entityFinderService.getTaskInProject(project,dto.getNameTask());

        project.getProjectTasks().remove(deleteTask);
        projectRepository.save(project);
        taskRepository.delete(deleteTask);

    }
    public void updateNameTask(UpdateNameTask dto){
        if(!entityFinderService.isUserOwnerAndAdmin(dto.getNameProject())){
            throw new RuntimeException("У вас недостаточно прав!");
        }
        Project project = entityFinderService.getProjectByName(dto.getNameProject());
        Task task = entityFinderService.getTaskInProject(project,dto.getOldNameTask());

        task.setTitle(dto.getNewNameTask());
        taskRepository.save(task);


    }
    public void updateDescriptionTask(UpdateDescription dto){
        Project project = entityFinderService.getProjectByName(dto.getNameProject());

        Task findTask = entityFinderService.getTaskInProject(project,dto.getNameTask());
        findTask.setDescription(dto.getNewDescription());
        taskRepository.save(findTask);
    }

    public void updateStatusTask(UpdateStatus dto){ // сделать выбор статусов
        Project project = entityFinderService.getProjectByName(dto.getTitleTask());

        Task findTask = entityFinderService.getTaskInProject(project,dto.getTitleTask());
        findTask.setStatus(dto.getStatus());
        taskRepository.save(findTask);
    }






}
