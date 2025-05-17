package com.example.Team.Task.Manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @Enumerated(EnumType.STRING)
    private Role roles;

    @ManyToMany
    @JsonBackReference
    @JoinTable(
            name = "user_projects", // название таблицы связи
            joinColumns = @JoinColumn(name = "user_id"), // внешний ключ на User
            inverseJoinColumns = @JoinColumn(name = "project_id") // внешний ключ на Project
    )
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProject> userProjects = new ArrayList<>();


}
