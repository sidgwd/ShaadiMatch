package com.shaadi.challenge.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "age_table")
public class Agedetails {
    @PrimaryKey(autoGenerate = true)
    private int ageId;
    private String date;
    private int age;

    public int getAgeId() {
        return ageId;
    }

    public void setAgeId(int ageId) {
        this.ageId = ageId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
