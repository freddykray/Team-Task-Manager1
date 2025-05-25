package com.example.Team.Task.Manager.repository;

import com.example.Team.Task.Manager.dtoProject.ProjectResponse;
import com.example.Team.Task.Manager.entity.Project;
import com.example.Team.Task.Manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
//    List<Project> findAllByOwner(User owner);
//    Optional<Project> findByUserIdAndProjectName(@Param("owner") Long userId, @Param("projectName") String projectName);
}