package com.tango.identino.model;

import java.util.List;

public class instructor {
    public  String name;
    public  String uid;
    public static String password;

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        instructor.password = password;
    }

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

    public instructor()
    {

    }

    public instructor(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }
}
