package com.shaadi.challenge.model;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "picture_table")
public class Picturedetails  {
    @PrimaryKey(autoGenerate = true)
    private int picId;
    private String large;

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }
}
