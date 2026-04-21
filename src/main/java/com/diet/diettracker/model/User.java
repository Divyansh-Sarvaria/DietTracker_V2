package com.diet.diettracker.model;

public class User {

    private String id;      
    private String name;
    private String email;
    private String passwordHash; 
    private long   createdAt;  

    public User() {}

    public User(String name, String email, String passwordHash) {
        this.name         = name;
        this.email        = email;
        this.passwordHash = passwordHash;
        this.createdAt    = System.currentTimeMillis();
    }

    // ── Getters & Setters ──────────────────────────────────────

    public String getId()            { return id; }
    public void   setId(String id)   { this.id = id; }

    public String getName()              { return name; }
    public void   setName(String name)   { this.name = name; }

    public String getEmail()               { return email; }
    public void   setEmail(String email)   { this.email = email; }

    public String getPasswordHash()                      { return passwordHash; }
    public void   setPasswordHash(String passwordHash)   { this.passwordHash = passwordHash; }

    public long getLong()                 { return createdAt; }
    public void setCreatedAt(long ts)     { this.createdAt = ts; }
}