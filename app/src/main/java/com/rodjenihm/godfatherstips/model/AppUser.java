package com.rodjenihm.godfatherstips.model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AppUser {
    private String userId;
    private String email;
    private Date createdAt;
    private List<String> roles = new ArrayList<>();
    private boolean isEmailVerified;

    public AppUser() {
    }

    public AppUser(String userId, String email, Date createdAt, List<String> roles, boolean isEmailVerified) {
        this.userId = userId;
        this.email = email;
        this.createdAt = createdAt;
        this.roles = roles;
        this.isEmailVerified = isEmailVerified;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public AppUser withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public AppUser withEmail(String email) {
        this.email = email;
        return this;
    }

    public AppUser withEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
        return this;
    }

    public AppUser withCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public AppUser withRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public AppUser withRole(String role) {
        this.roles.add(role);
        return this;
    }
}
