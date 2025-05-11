package com.example.Team.Task.Manager.dtoProject;

import com.example.Team.Task.Manager.entity.Role;
import lombok.Data;

@Data
public  class SignupRequest {
   private String username;
   private String email;
   private String password;
   private Role roles;


}
