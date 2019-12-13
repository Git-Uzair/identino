package com.tango.identino.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attendance_record {
    private String regno, name;
    private int present, absent;


    public Attendance_record() {
    }

    public Attendance_record(String regno, String name, int present, int absent) {
        this.regno = regno;
        this.name = name;
        this.present = present;
        this.absent = absent;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }
}


