package com.example.Team.Task.Manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference
    private Project project;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false) // Внешний ключ на пользователя, который является владельцем проекта
    @JsonBackReference
    private User assignee;

    @Column(name = "date_time")
    private LocalDateTime datetime;

    @Column(name = "is_notification_sent")
    private boolean isNotificationSent;

}
