package com.example.Team.Task.Manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToOne // Один проект имеет одного владельца
    @JoinColumn(name = "owner", nullable = false) // Внешний ключ на пользователя, который является владельцем проекта
    @JsonBackReference
    private User owner;

    @Column
    private LocalDateTime datetime;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> projectTasks = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserProject> userProjects = new ArrayList<>();

    @JsonProperty("owner")
    public String getOwnerUsername() {
        return owner.getUsername();
    }


}
