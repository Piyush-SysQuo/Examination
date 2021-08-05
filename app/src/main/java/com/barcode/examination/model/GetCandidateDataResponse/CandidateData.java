package com.barcode.examination.model.GetCandidateDataResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class CandidateData {

    @SerializedName("applicationNumber")
    @Expose
    private String applicationNumber;
    @SerializedName("candidateID")
    @Expose
    private String candidateID;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("centreCode")
    @Expose
    private String centreCode;
    @SerializedName("centreName")
    @Expose
    private String centreName;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("dateOfExam")
    @Expose
    private String dateOfExam;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("examEndTime")
    @Expose
    private String examEndTime;
    @SerializedName("examStartTime")
    @Expose
    private String examStartTime;
    @SerializedName("faceImageURL")
    @Expose
    private String faceImageURL;
    @SerializedName("faceStatus")
    @Expose
    private String faceStatus;
    @SerializedName("imageURL")
    @Expose
    private String imageURL;
    @SerializedName("irisImageURL")
    @Expose
    private String irisImageURL;
    @SerializedName("irisStatus")
    @Expose
    private String irisStatus;
    @SerializedName("labAllotted")
    @Expose
    private String labAllotted;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("occurrance")
    @Expose
    private Integer occurrance;
    @SerializedName("reportingTime")
    @Expose
    private String reportingTime;
    @SerializedName("rollNumber")
    @Expose
    private String rollNumber;
    @SerializedName("seatAllotted")
    @Expose
    private String seatAllotted;
    @SerializedName("shift")
    @Expose
    private String shift;
    @SerializedName("state")
    @Expose
    private String state;

    public CandidateData() {
    }

    public CandidateData(String applicationNumber, String candidateID, String category, String centreCode, String centreName, String city, String dateOfExam, String dob, String examEndTime, String examStartTime, String faceImageURL, String faceStatus, String imageURL, String irisImageURL, String irisStatus, String labAllotted, String name, Integer occurrance, String reportingTime, String rollNumber, String seatAllotted, String shift, String state) {
        this.applicationNumber = applicationNumber;
        this.candidateID = candidateID;
        this.category = category;
        this.centreCode = centreCode;
        this.centreName = centreName;
        this.city = city;
        this.dateOfExam = dateOfExam;
        this.dob = dob;
        this.examEndTime = examEndTime;
        this.examStartTime = examStartTime;
        this.faceImageURL = faceImageURL;
        this.faceStatus = faceStatus;
        this.imageURL = imageURL;
        this.irisImageURL = irisImageURL;
        this.irisStatus = irisStatus;
        this.labAllotted = labAllotted;
        this.name = name;
        this.occurrance = occurrance;
        this.reportingTime = reportingTime;
        this.rollNumber = rollNumber;
        this.seatAllotted = seatAllotted;
        this.shift = shift;
        this.state = state;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public String getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(String candidateID) {
        this.candidateID = candidateID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCentreCode() {
        return centreCode;
    }

    public void setCentreCode(String centreCode) {
        this.centreCode = centreCode;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDateOfExam() {
        return dateOfExam;
    }

    public void setDateOfExam(String dateOfExam) {
        this.dateOfExam = dateOfExam;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getExamEndTime() {
        return examEndTime;
    }

    public void setExamEndTime(String examEndTime) {
        this.examEndTime = examEndTime;
    }

    public String getExamStartTime() {
        return examStartTime;
    }

    public void setExamStartTime(String examStartTime) {
        this.examStartTime = examStartTime;
    }

    public String getFaceImageURL() {
        return faceImageURL;
    }

    public void setFaceImageURL(String faceImageURL) {
        this.faceImageURL = faceImageURL;
    }

    public String getFaceStatus() {
        return faceStatus;
    }

    public void setFaceStatus(String faceStatus) {
        this.faceStatus = faceStatus;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getIrisImageURL() {
        return irisImageURL;
    }

    public void setIrisImageURL(String irisImageURL) {
        this.irisImageURL = irisImageURL;
    }

    public String getIrisStatus() {
        return irisStatus;
    }

    public void setIrisStatus(String irisStatus) {
        this.irisStatus = irisStatus;
    }

    public String getLabAllotted() {
        return labAllotted;
    }

    public void setLabAllotted(String labAllotted) {
        this.labAllotted = labAllotted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOccurrance() {
        return occurrance;
    }

    public void setOccurrance(Integer occurrance) {
        this.occurrance = occurrance;
    }

    public String getReportingTime() {
        return reportingTime;
    }

    public void setReportingTime(String reportingTime) {
        this.reportingTime = reportingTime;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getSeatAllotted() {
        return seatAllotted;
    }

    public void setSeatAllotted(String seatAllotted) {
        this.seatAllotted = seatAllotted;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}