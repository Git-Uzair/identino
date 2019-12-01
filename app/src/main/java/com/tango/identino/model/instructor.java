package com.tango.identino.model;

import java.util.List;

public class instructor {
    private String name;
    private String uid;

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
