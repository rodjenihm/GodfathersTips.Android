package com.rodjenihm.godfatherstips.model;

import com.google.firebase.Timestamp;


public class AppUser {
    public String userId;
    public String email;
    public Timestamp createdAt;
    public String role;
    public boolean isEmailVerified;

    public AppUser() {
    }

    public AppUser(String userId, String email, Timestamp createdAt, String role, boolean isEmailVerified) {
        this.userId = userId;
        this.email = email;
        this.createdAt = createdAt;
        this.role = role;
        this.isEmailVerified = isEmailVerified;
    }
}
