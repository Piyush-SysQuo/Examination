package com.barcode.examination.model.ScanWithFaceResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("candidateData")
    @Expose
    private CandidateData candidateData;
    @SerializedName("candidatesDataID")
    @Expose
    private String candidatesDataID;
    @SerializedName("centerCode")
    @Expose
    private String centerCode;
    @SerializedName("examName")
    @Expose
    private String examName;
    @SerializedName("faceStatus")
    @Expose
    private String faceStatus;
    @SerializedName("confidence")
    @Expose
    private double confidence;

    public CandidateData getCandidateData() {
        return candidateData;
    }

    public void setCandidateData(CandidateData candidateData) {
        this.candidateData = candidateData;
    }

    public String getCandidatesDataID() {
        return candidatesDataID;
    }

    public void setCandidatesDataID(String candidatesDataID) {
        this.candidatesDataID = candidatesDataID;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getFaceStatus() {
        return faceStatus;
    }

    public void setFaceStatus(String faceStatus) {
        this.faceStatus = faceStatus;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}