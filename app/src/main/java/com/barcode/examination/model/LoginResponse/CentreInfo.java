package com.barcode.examination.model.LoginResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CentreInfo {

    @SerializedName("centerCode")
    @Expose
    private String centerCode;
    @SerializedName("centerName")
    @Expose
    private String centerName;
    @SerializedName("currentExamDate")
    @Expose
    private String currentExamDate;
    @SerializedName("currentExamShift")
    @Expose
    private String currentExamShift;
    @SerializedName("examEndTime")
    @Expose
    private String examEndTime;
    @SerializedName("examID")
    @Expose
    private String examID;
    @SerializedName("examName")
    @Expose
    private String examName;
    @SerializedName("examStartTime")
    @Expose
    private String examStartTime;
    @SerializedName("faceAuth")
    @Expose
    private Boolean faceAuth;
    @SerializedName("irisAuth")
    @Expose
    private Boolean irisAuth;
    @SerializedName("modules")
    @Expose
    private List<String> modules = null;
    @SerializedName("reportingTime")
    @Expose
    private String reportingTime;

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getCurrentExamDate() {
        return currentExamDate;
    }

    public void setCurrentExamDate(String currentExamDate) {
        this.currentExamDate = currentExamDate;
    }

    public String getCurrentExamShift() {
        return currentExamShift;
    }

    public void setCurrentExamShift(String currentExamShift) {
        this.currentExamShift = currentExamShift;
    }

    public String getExamEndTime() {
        return examEndTime;
    }

    public void setExamEndTime(String examEndTime) {
        this.examEndTime = examEndTime;
    }

    public String getExamID() {
        return examID;
    }

    public void setExamID(String examID) {
        this.examID = examID;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamStartTime() {
        return examStartTime;
    }

    public void setExamStartTime(String examStartTime) {
        this.examStartTime = examStartTime;
    }

    public Boolean getFaceAuth() {
        return faceAuth;
    }

    public void setFaceAuth(Boolean faceAuth) {
        this.faceAuth = faceAuth;
    }

    public Boolean getIrisAuth() {
        return irisAuth;
    }

    public void setIrisAuth(Boolean irisAuth) {
        this.irisAuth = irisAuth;
    }

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public String getReportingTime() {
        return reportingTime;
    }

    public void setReportingTime(String reportingTime) {
        this.reportingTime = reportingTime;
    }

}
