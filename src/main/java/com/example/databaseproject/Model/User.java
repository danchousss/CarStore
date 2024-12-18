package com.example.databaseproject.Model;

public class User {
    private boolean isAdmin;
    private int id;

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public int getUserId() {
        return id;
    }

    public User (int id, boolean isAdmin) {
        this.isAdmin = isAdmin;
        this.id = id;
    }
}
