package com.tango.identino.model;

import java.util.HashMap;

public class Attendance_record {
    private String regno, name;
    private int present, absent;
    private HashMap<String, Integer> attendance;

    public Attendance_record() {
    }

    public Attendance_record(String regno, String name, int present, int absent, HashMap<String, Integer> attendance) {
        this.regno = regno;
        this.name = name;
        this.present = present;
        this.absent = absent;
        this.attendance = attendance;
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


