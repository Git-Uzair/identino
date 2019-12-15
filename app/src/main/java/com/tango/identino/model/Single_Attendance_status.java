package com.tango.identino.model;

public class Single_Attendance_status {
    private String regno;
    private int status;

    public Single_Attendance_status() {
    }

    public Single_Attendance_status(String regno, int status) {
        this.regno = regno;
        this.status = status;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
