package com.example.Team.Task.Manager.entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class Mail {
    private String to;
    private String subject;
    private String body;
}
