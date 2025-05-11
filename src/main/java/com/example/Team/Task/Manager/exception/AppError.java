package com.example.Team.Task.Manager.exception;

import lombok.Data;

import java.util.Date;

@Data
public class AppError extends RuntimeException {
    private int status;
    private String message;
    private Date timeStamp;

    public AppError(int status, String message){
        this.status = status;
        this.message = message;
        this.timeStamp = new Date();
    }
}
