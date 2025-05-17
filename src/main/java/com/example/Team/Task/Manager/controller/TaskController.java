package com.example.Team.Task.Manager.controller;

import com.example.Team.Task.Manager.dtoTask.*;
import com.example.Team.Task.Manager.entity.Task;
import com.example.Team.Task.Manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/addTask")
    public Task createTask(@RequestBody TaskRequest dto){
        return taskService.createTask(dto);
    }

    @DeleteMapping("/deleteTask")
    public void deleteTask(@RequestBody DeleteTaskDTO dto){
        taskService.deleteTask(dto);
    }
    @PutMapping("/updateTaskName")
    public void updateNameTask(@RequestBody UpdateNameTask dto){
        taskService.updateNameTask(dto);
    }
    @PutMapping("/updateDescription")
    public void updateDescriptionTask(@RequestBody UpdateDescription dto){
        taskService.updateDescriptionTask(dto);
    }
    @PutMapping("/updateStatus")
    public void updateStatusTask(@RequestBody UpdateStatus dto){
        taskService.updateStatusTask(dto);
        String message = String.format("Task status updated: %s", dto.getStatus().toString());
        kafkaProducerService.sendMessage(message);
    }



}
