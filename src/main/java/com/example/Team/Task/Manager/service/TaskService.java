package com.example.Team.Task.Manager.service;


import com.example.Team.Task.Manager.dtoTask.DeleteTask;
import com.example.Team.Task.Manager.dtoTask.TaskRequest;
import com.example.Team.Task.Manager.dtoTask.UpdateDescription;
import com.example.Team.Task.Manager.dtoTask.UpdateNameTask;
import com.example.Team.Task.Manager.dtoTask.UpdateStatus;
import com.example.Team.Task.Manager.entity.Mail;
import com.example.Team.Task.Manager.entity.Project;
import com.example.Team.Task.Manager.entity.Task;
import com.example.Team.Task.Manager.entity.User;
import com.example.Team.Task.Manager.entity.UserProject;
import com.example.Team.Task.Manager.kafka.KafkaProducer;
import com.example.Team.Task.Manager.repository.ProjectRepository;
import com.example.Team.Task.Manager.repository.TaskRepository;
import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final EntityFinder entityFinder;
    private final KafkaProducer kafkaProducerService;

    public Task createTask(TaskRequest dto) {
        if (!entityFinder.isUserOwnerAndAdmin(dto.getNameProject())) {
            throw new RuntimeException("У вас недостаточно прав!");
        }

        Project project = entityFinder.getProjectByName(dto.getNameProject());
        List<Task> tasks = taskRepository.findAllByProject(project);

        boolean existTaskName = tasks.stream()
                .anyMatch(task -> task.getTitle().equals(dto.getTitle()));

        if (existTaskName) {
            throw new RuntimeException("Такое имя задания уже занято!");
        }

        User assignee = entityFinder.getUserByName(dto.getAssignee());
        Optional<UserProject> userProjectOptional = entityFinder.userInProject(assignee, project);

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setProject(project);
        task.setDatetime(dto.getDateTime());
        task.setAssignee(assignee);

        Task savedTask = taskRepository.save(task);
        project.getProjectTasks().add(savedTask);
        projectRepository.save(project);

        return savedTask;
    }

    public void deleteTask(DeleteTask dto) {
        if (!entityFinder.isUserOwnerAndAdmin(dto.getNameProject())) {
            throw new RuntimeException("У вас недостаточно прав!");
        }

        Project project = entityFinder.getProjectByName(dto.getNameProject());
        Task deleteTask = entityFinder.getTaskInProject(project, dto.getNameTask());

        project.getProjectTasks().remove(deleteTask);
        projectRepository.save(project);
        taskRepository.delete(deleteTask);
    }

    public void updateNameTask(UpdateNameTask dto) {
        if (!entityFinder.isUserOwnerAndAdmin(dto.getNameProject())) {
            throw new RuntimeException("У вас недостаточно прав!");
        }

        Project project = entityFinder.getProjectByName(dto.getNameProject());
        Task task = entityFinder.getTaskInProject(project, dto.getOldNameTask());

        task.setTitle(dto.getNewNameTask());
        taskRepository.save(task);
    }

    public void updateDescriptionTask(UpdateDescription dto) {
        if (!entityFinder.isUserOwnerAndAdmin(dto.getNameProject())) {
            throw new RuntimeException("У вас недостаточно прав!");
        }

        Project project = entityFinder.getProjectByName(dto.getNameProject());
        Task task = entityFinder.getTaskInProject(project, dto.getNameTask());

        task.setDescription(dto.getNewDescription());
        taskRepository.save(task);
    }

    public void updateStatusTask(UpdateStatus dto) {
        Project project = entityFinder.getProjectByName(dto.getProjectName());
        Task task = entityFinder.getTaskInProject(project, dto.getTitleTask());

        // Получаем текущего пользователя
        User user = entityFinder.getUserByName(project.getOwnerUsername());

        // Подготовка письма
        Mail mail = new Mail();
        mail.setTo(user.getEmail());
        mail.setSubject("Team Task Manager");
        mail.setBody(String.format(
                "В вашем проекте '%s' статус задачи '%s' был изменен на '%s'",
                project.getName(),
                task.getTitle(),
                dto.getStatus()
        ));

        kafkaProducerService.sendMail(mail);

        // Обновляем статус задачи
        task.setStatus(dto.getStatus());
        taskRepository.save(task);
    }
}
