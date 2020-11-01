package com.shaadi.challenge.model;



import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class UserDataModel {
    @PrimaryKey(autoGenerate = true)
    private int userId;
    private String gender;
    @Embedded
    private NameDetails name;
    @Embedded
    private Picturedetails picture;
    private String email;
    private String phone;
    private String cell;
    private int approveStatus=0;
    @Embedded
    private LocationDetails location;
    @Embedded
    private Agedetails dob;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public NameDetails getName() {
        return name;
    }

    public void setName(NameDetails name) {
        this.name = name;
    }

    public Picturedetails getPicture() {
        return picture;
    }

    public void setPicture(Picturedetails picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public LocationDetails getLocation() {
        return location;
    }

    public void setLocation(LocationDetails location) {
        this.location = location;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Agedetails getDob() {
        return dob;
    }

    public void setDob(Agedetails dob) {
        this.dob = dob;
    }

    public int getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
    }
}
