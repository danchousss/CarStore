package com.example.databaseproject;

import com.example.databaseproject.Model.Customer;
import com.example.databaseproject.Model.User;

public class UserSession {
    private static User user;
    private UserSession(){

    }

    public static User getUser () {
        return user;
    }

    public static void setUser (User user) {
        UserSession.user = user;
    }
}
