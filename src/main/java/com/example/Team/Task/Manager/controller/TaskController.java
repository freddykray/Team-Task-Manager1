package com.example.Team.Task.Manager.controller;


import com.example.Team.Task.Manager.dtoTask.TaskRequest;
import com.example.Team.Task.Manager.dtoTask.UpdateDescription;
import com.example.Team.Task.Manager.dtoTask.UpdateNameTask;
import com.example.Team.Task.Manager.dtoTask.UpdateStatus;
import com.example.Team.Task.Manager.entity.Task;
import com.example.Team.Task.Manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // ✅ Создание задачи в проекте
    @PostMapping("/projects/{projectId}/tasks")
    public Task createTask(@PathVariable Long projectId, @RequestBody TaskRequest dto) {
        return taskService.createTask(projectId, dto);
    }

    // ✅ Удаление задачи в проекте
    @DeleteMapping("/projects/{projectId}/tasks/{taskId}")
    public void deleteTask(@PathVariable Long projectId, @PathVariable Long taskId) {
        taskService.deleteTask(projectId, taskId);
    }

    // ✅ Обновление имени задачи
    @PutMapping("/projects/{projectId}/tasks/{taskId}/name")
    public void updateNameTask(@PathVariable Long projectId, @PathVariable Long taskId, @RequestBody UpdateNameTask dto) {
        taskService.updateNameTask(projectId, taskId, dto);
    }

    // ✅ Обновление описания задачи
    @PutMapping("/projects/{projectId}/tasks/{taskId}/description")
    public void updateDescriptionTask(@PathVariable Long projectId, @PathVariable Long taskId, @RequestBody UpdateDescription dto) {
        taskService.updateDescriptionTask(projectId, taskId, dto);
    }

    // ✅ Обновление статуса задачи
    @PutMapping("/projects/{projectId}/tasks/{taskId}/status")
    public void updateStatusTask(@PathVariable Long projectId, @PathVariable Long taskId, @RequestBody UpdateStatus dto) {
        taskService.updateStatusTask(projectId, taskId, dto);
    }


}
