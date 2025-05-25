package com.example.Team.Task.Manager.service;


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

    public Task createTask(Long projectId,TaskRequest dto) {
        if (!entityFinder.isUserOwnerAndAdmin(projectId)) {
            throw new RuntimeException("У вас недостаточно прав!");
        }

        Project project = entityFinder.findById(projectId);
        List<Task> tasks = taskRepository.findAllByProject(project);

        boolean existTaskName = tasks.stream()
                .anyMatch(task -> task.getTitle().equals(dto.getTitle()));

        if (existTaskName) {
            throw new RuntimeException("Такое имя задания уже занято!");
        }

        User assignee = entityFinder.getUserByName(dto.getAssignee());

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

    public void deleteTask(Long projectId, Long taskId) {


        if (!entityFinder.isUserOwnerAndAdmin(projectId)) {
            throw new RuntimeException("У вас недостаточно прав!");
        }

        Task TaskAndProject = entityFinder.getTaskByIdInProject(projectId, taskId);

        TaskAndProject.getProject().getProjectTasks().remove(TaskAndProject);
        projectRepository.save(TaskAndProject.getProject());
        taskRepository.delete(TaskAndProject);
    }

    public void updateNameTask(Long projectId, Long taskId, UpdateNameTask dto) {

        if (!entityFinder.isUserOwnerAndAdmin(projectId)) {
            throw new RuntimeException("У вас недостаточно прав!");
        }

        Task task = entityFinder.getTaskByIdInProject(projectId, taskId);

        task.setTitle(dto.getNewNameTask());
        taskRepository.save(task);
    }

    public void updateDescriptionTask(Long projectId, Long taskId,UpdateDescription dto) {
        if (!entityFinder.isUserOwnerAndAdmin(projectId)) {
            throw new RuntimeException("У вас недостаточно прав!");
        }

        Task task = entityFinder.getTaskByIdInProject(projectId, taskId);


            task.setDescription(dto.getNewDescription());


        taskRepository.save(task);
    }

    public void updateStatusTask(Long projectId, Long taskId, UpdateStatus dto) {

        Task task = entityFinder.getTaskByIdInProject(projectId, taskId);

        // Получаем текущего пользователя
        User user = entityFinder.getUserByName(task.getProject().getOwnerUsername());

        // Подготовка письма
        Mail mail = new Mail();
        mail.setTo(user.getEmail());
        mail.setSubject("Team Task Manager");
        mail.setBody(String.format(
                "В вашем проекте '%s' статус задачи '%s' был изменен на '%s'",
                task.getProject().getName(),
                task.getTitle(),
                dto.getStatus()
        ));

        kafkaProducerService.sendMail(mail);

        // Обновляем статус задачи
        task.setStatus(dto.getStatus());
        taskRepository.save(task);
    }
}
