package com.example.forteatchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
    public String name;
    public String surname;
    public String email;
    public String profilePicture;
    public boolean isTeacher;
    public HashMap<String,Class> classes;
    public String id;

    public User() {
    }

    public User(String name, String surname, String email, String profilePicture, boolean isTeacher, String id) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.profilePicture = profilePicture;
        this.isTeacher = isTeacher;
        this.id = id;
    }
}
