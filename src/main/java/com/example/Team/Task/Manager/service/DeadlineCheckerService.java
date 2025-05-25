package com.example.Team.Task.Manager.service;

import com.example.Team.Task.Manager.entity.Mail;
import com.example.Team.Task.Manager.entity.Task;
import com.example.Team.Task.Manager.kafka.KafkaProducer;
import com.example.Team.Task.Manager.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class DeadlineCheckerService {

    private  TaskRepository taskRepository;

    private  KafkaProducer kafkaProducer;

    @Scheduled(initialDelay = 0, fixedRate = 60 * 60 * 1000)
    public void checkDeadlineTask(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDay = now.plusDays(1);

        List<Task> tasks = taskRepository.findTasksWithDeadlineBetween(now, nextDay);

        for (Task task : tasks){
            if (!task.isNotificationSent()){
                String emailAssignee =  task.getAssignee().getEmail();

                Mail mail = new Mail();
                mail.setTo(emailAssignee);
                mail.setSubject("Напоминание о дедлайне");
                mail.setBody(String.format("Задача '%s', в которой вы являетесь исполнителем, истекает через один день", task.getTitle()));

                kafkaProducer.sendMail(mail);

               task.setNotificationSent(true);
               taskRepository.save(task);
            }
        }


    }



}
