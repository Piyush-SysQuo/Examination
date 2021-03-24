package com.inspection.examination.model.GetAbsentCandidatesResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("candidatesDataID")
    @Expose
    private String candidatesDataID;
    @SerializedName("candidatesList")
    @Expose
    private List<AbsentCandidatesList> candidatesList = null;
    @SerializedName("centerCode")
    @Expose
    private String centerCode;
    @SerializedName("examName")
    @Expose
    private String examName;
    @SerializedName("hasNext")
    @Expose
    private Boolean hasNext;
    @SerializedName("hasPrev")
    @Expose
    private Boolean hasPrev;

    public String getCandidatesDataID() {
        return candidatesDataID;
    }

    public void setCandidatesDataID(String candidatesDataID) {
        this.candidatesDataID = candidatesDataID;
    }

    public List<AbsentCandidatesList> getCandidatesList() {
        return candidatesList;
    }

    public void setCandidatesList(List<AbsentCandidatesList> candidatesList) {
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

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Boolean getHasPrev() {
        return hasPrev;
    }

    public void setHasPrev(Boolean hasPrev) {
        this.hasPrev = hasPrev;
    }

}