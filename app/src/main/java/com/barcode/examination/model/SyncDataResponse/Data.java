package com.barcode.examination.model.SyncDataResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("candidatesDataID")
    @Expose
    private String candidatesDataID;
    @SerializedName("candidatesList")
    @Expose
    private List<CandidatesList> candidatesList = null;
    @SerializedName("centerCode")
    @Expose
    private String centerCode;
    @SerializedName("examName")
    @Expose
    private String examName;

    public String getCandidatesDataID() {
        return candidatesDataID;
    }

    public void setCandidatesDataID(String candidatesDataID) {
        this.candidatesDataID = candidatesDataID;
    }

    public List<CandidatesList> getCandidatesList() {
        return candidatesList;
    }

    public void setCandidatesList(List<CandidatesList> candidatesList) {
        this.candidatesList = candidatesList;
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

}