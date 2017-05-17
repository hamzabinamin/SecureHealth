package com.HPA.securehealth;

/**
 * Created by Hamza on 11/24/2016.
 */

public class User {

    private String name;
    private String email;
    private String password;
    private String gender;
    private String dob;
    private String height;
    private String weight;
    private String activityLevel;
    private int waterCount;

    public User() {
        this.name = "";
        this.email = "";
        this.password = "";
        this.gender = "";
        this.dob = "";
        this.height = "";
        this.weight = "";
        this.activityLevel = "";
        this.waterCount = 0;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDOB(String dob) {
            this.dob = dob;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public void setWaterCount(int count) {
        this.waterCount = count;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getGender() {
        return this.gender;
    }

    public String getDOB() {
        return this.dob;
    }

    public String getHeight() {
        return this.height;
    }

    public String getWeight() {
        return this.weight;
    }

    public String getActivityLevel() {
        return this.activityLevel;
    }

    public int getWaterCount() {
        return this.waterCount;
    }

    public String toString() {
        return "Name: " + this.name + " Email: " + this.email + "Password: " + this.password;
    }

}
