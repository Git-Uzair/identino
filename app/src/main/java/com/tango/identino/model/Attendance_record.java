package com.tango.identino.model;

public class Attendance_record {
    private String regno;
    private int present, absent;

    public Attendance_record() {
    }

    public Attendance_record(String regno, int present, int absent) {
        this.regno = regno;
        this.present = present;
        this.absent = absent;
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
