package com.example.progass2.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Access {

    int accessId;
    int studentId;
    String accessType;
    String timeStamp;

    public Access() {}

    public Access(int accessId, int studentId, String accessType, String timeStamp) {
        this.accessId = accessId;
        this.studentId = studentId;
        this.accessType = accessType;
        this.timeStamp = timeStamp;
    }

    // convenient for quick insert in database
    public Access(int studentId, String accessType) {
        this.studentId = studentId;
        this.accessType = accessType;
        this.timeStamp = new SimpleDateFormat("yyyy-MM-dd @ HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public int getAccessId() {
        return accessId;
    }

    public void setAccessId(int accessId) {
        this.accessId = accessId;
    }

    //for easy printing
    public String toString() {
        return "Access{" +
                "accessId=" + accessId +
                ", profileId=" + studentId +
                ", accessType='" + accessType + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

