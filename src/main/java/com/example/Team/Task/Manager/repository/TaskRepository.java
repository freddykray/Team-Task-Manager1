package com.example.Team.Task.Manager.repository;

import com.example.Team.Task.Manager.entity.Project;
import com.example.Team.Task.Manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findAllByProject(Project project);

    @Query("SELECT t FROM Task t WHERE t.date_time BETWEEN :now AND :nextDay")
    List<Task> findTasksWithDeadlineBetween(@Param("now") LocalDateTime now, @Param("nextDay") LocalDateTime nextDay);

}
