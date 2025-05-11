package com.example.Team.Task.Manager.repository;

import com.example.Team.Task.Manager.entity.Project;
import com.example.Team.Task.Manager.entity.User;
import com.example.Team.Task.Manager.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    Optional<UserProject> findByUserAndProject(User user, Project project);
    boolean existsByUserAndProject(User user, Project project);
    List<UserProject> findAllByUser(User user);

}
