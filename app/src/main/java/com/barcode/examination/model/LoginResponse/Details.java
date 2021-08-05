package com.barcode.examination.model.LoginResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Details {

    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phoneCode")
    @Expose
    private Object phoneCode;
    @SerializedName("phoneNumber")
    @Expose
    private Object phoneNumber;
    @SerializedName("userID")
    @Expose
    private String userID;

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(Object phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Object getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Object phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}