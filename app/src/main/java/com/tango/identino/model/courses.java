package com.tango.identino.model;

import java.util.List;

public class courses {
    private List<String> Courses;

    public courses() {

    }

    public List<String> getCourses() {
        return Courses;
    }

    public void setCourses(List<String> courses) {
        Courses = courses;
    }

    public courses(List<String> Courses) {
        this.Courses = Courses;
    }
}
