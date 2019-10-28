package com.rodjenihm.godfatherstips.model;

import com.google.firebase.Timestamp;

import java.util.List;


public class AppUser {
    private String userId;
    private String email;
    private Timestamp createdAt;
    private List<String> roles;
    private boolean isEmailVerified;

    public AppUser() {
    }

    public AppUser(String userId, String email, Timestamp createdAt, List<String> roles, boolean isEmailVerified) {
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
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
}
