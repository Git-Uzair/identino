package com.tango.identino.model;

import java.util.List;

public class instructor {
    public  String name;
    public  String uid;
    public Boolean admin;
    public static String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        instructor.password = password;
    }

    public instructor()
    {

    }

    public instructor(String name, String uid, Boolean admin) {
        this.name = name;
        this.uid = uid;
        this.admin = admin;
    }
}
