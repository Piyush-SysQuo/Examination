package com.barcode.examination.model.LoginResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("centreInfo")
    @Expose
    private CentreInfo centreInfo;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("userData")
    @Expose
    private UserData userData;

    public CentreInfo getCentreInfo() {
        return centreInfo;
    }

    public void setCentreInfo(CentreInfo centreInfo) {
        this.centreInfo = centreInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

}