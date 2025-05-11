package com.example.Team.Task.Manager.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, ROLE_OWNER;

    @Override
    public String getAuthority() {
        return name();
    }
}
