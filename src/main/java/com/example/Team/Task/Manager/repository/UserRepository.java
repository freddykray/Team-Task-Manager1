package com.example.Team.Task.Manager.repository;

import com.example.Team.Task.Manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
   Optional<User> findById(Long id);
   Optional<User> findUserByUsername(String username);
   Boolean existsUserByUsername(String username);
   Boolean existsUserByEmail(String email);



}
