package com.example.Team.Task.Manager.repository;

import com.example.Team.Task.Manager.entity.Project;
import com.example.Team.Task.Manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findAllByProject(Project project);
}
