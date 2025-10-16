package com.example.progass2.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Profile {
    int studentId;
    String surname;
    String name;
    float gpa;
    String creationDate;

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Profile() {}


    public Profile(int studentId, String surname, String name, float gpa, String creationDate) {
        this.studentId = studentId;
        this.surname = surname;
        this.name = name;
        this.gpa = gpa;
        this.creationDate = creationDate;
    }

    public Profile(int profileId, String surname, String name, float gpa) {
        this.studentId = profileId;
        this.surname = surname;
        this.name = name;
        this.gpa = gpa;
        this.creationDate = new SimpleDateFormat("yyyy-MM-dd @ HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }

    public int getStudentId() {
        return studentId;
    }

    public float getGpa() {
        return gpa;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    //easy printing
    @Override
    public String toString() {
        return surname + ", " + name + " (" + studentId + ")";
    }
}
