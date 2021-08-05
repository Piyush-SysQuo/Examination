package com.barcode.examination.model.LoginResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("activeStatus")
    @Expose
    private Integer activeStatus;
    @SerializedName("centerCode")
    @Expose
    private String centerCode;
    @SerializedName("details")
    @Expose
    private Details details;
    @SerializedName("examID")
    @Expose
    private String examID;
    @SerializedName("joinDate")
    @Expose
    private String joinDate;
    @SerializedName("orgID")
    @Expose
    private String orgID;
    @SerializedName("orgName")
    @Expose
    private String orgName;
    @SerializedName("orgShortName")
    @Expose
    private String orgShortName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("profileImage")
    @Expose
    private String profileImage;
    @SerializedName("roleClass")
    @Expose
    private String roleClass;
    @SerializedName("roleID")
    @Expose
    private String roleID;
    @SerializedName("roleName")
    @Expose
    private String roleName;
    @SerializedName("sessionID")
    @Expose
    private String sessionID;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("username")
    @Expose
    private String username;

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public String getExamID() {
        return examID;
    }

    public void setExamID(String examID) {
        this.examID = examID;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgShortName() {
        return orgShortName;
    }

    public void setOrgShortName(String orgShortName) {
        this.orgShortName = orgShortName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getRoleClass() {
        return roleClass;
    }

    public void setRoleClass(String roleClass) {
        this.roleClass = roleClass;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}