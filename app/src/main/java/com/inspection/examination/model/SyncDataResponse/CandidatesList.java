package com.inspection.examination.model.SyncDataResponse;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.inspection.examination.util.Constants;

import org.parceler.Parcel;

@Entity(tableName = Constants.KEY_TABLE_NAME)
@Parcel
public class CandidatesList {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = Constants.KEY_CANDIDATE_ID)
    @SerializedName("candidateID")
    @Expose
    private String candidateID;

    @ColumnInfo(name = Constants.KEY_APPLICATION_NUMBER)
    @SerializedName("applicationNumber")
    @Expose
    private String applicationNumber;

    @ColumnInfo(name = Constants.KEY_CATEGORY)
    @SerializedName("category")
    @Expose
    private String category;

    @ColumnInfo(name = Constants.KEY_CENTRE_CODE)
    @SerializedName("centreCode")
    @Expose
    private String centreCode;

    @ColumnInfo(name = Constants.KEY_CENTRE_NAME)
    @SerializedName("centreName")
    @Expose
    private String centreName;

    @ColumnInfo(name = Constants.KEY_CITY)
    @SerializedName("city")
    @Expose
    private String city;

    @ColumnInfo(name = Constants.KEY_DATE_OF_EXAM)
    @SerializedName("dateOfExam")
    @Expose
    private String dateOfExam;

    @ColumnInfo(name = Constants.KEY_DOB)
    @SerializedName("dob")
    @Expose
    private String dob;

    @ColumnInfo(name = Constants.KEY_EXAM_END_TIME)
    @SerializedName("examEndTime")
    @Expose
    private String examEndTime;

    @ColumnInfo(name = Constants.KEY_EXAM_START_TIME)
    @SerializedName("examStartTime")
    @Expose
    private String examStartTime;

    @ColumnInfo(name = Constants.KEY_IMAGE_STRING)
    @SerializedName("imageString")
    @Expose
    private String imageString;

    @ColumnInfo(name = Constants.KEY_LAB_ALLOTTED)
    @SerializedName("labAllotted")
    @Expose
    private String labAllotted;

    @ColumnInfo(name = Constants.KEY_NAME)
    @SerializedName("name")
    @Expose
    private String name;

    @ColumnInfo(name = Constants.KEY_OCCURRANCE)
    @SerializedName("occurrance")
    @Expose
    private Integer occurrance;

    @ColumnInfo(name = Constants.KEY_REPORTING_TIME)
    @SerializedName("reportingTime")
    @Expose
    private String reportingTime;

    @ColumnInfo(name = Constants.KEY_ROLL_NUMBER)
    @SerializedName("rollNumber")
    @Expose
    private String rollNumber;

    @ColumnInfo(name = Constants.KEY_SEAT_ALLOTTED)
    @SerializedName("seatAllotted")
    @Expose
    private String seatAllotted;

    @ColumnInfo(name = Constants.KEY_SHIFT)
    @SerializedName("shift")
    @Expose
    private String shift;

    @ColumnInfo(name = Constants.KEY_STATE)
    @SerializedName("state")
    @Expose
    private String state;

    @ColumnInfo(name = Constants.KEY_FACE_IMAGE)
    @SerializedName("faceImageString")
    @Expose
    private String faceImageString;

    @ColumnInfo(name = Constants.KEY_IRIS_IMAGE)
    @SerializedName("irisImageString")
    @Expose
    private String irisImageString;

    @ColumnInfo(name = Constants.KEY_FACE_STATUS)
    @SerializedName("faceStatus")
    @Expose
    private String faceStatus;

    @ColumnInfo(name = Constants.KEY_IRIS_STATUS)
    @SerializedName("irisStatus")
    @Expose
    private String irisStatus;

    public CandidatesList() {
    }

//    public CandidatesList(@NonNull String candidateID, String applicationNumber, String category, String centreCode, String centreName, String city, String dateOfExam, String dob, String examEndTime, String examStartTime, String imageString, String labAllotted, String name, Integer occurrance, String reportingTime, String rollNumber, String seatAllotted, String shift, String state) {
//        this.candidateID = candidateID;
//        this.applicationNumber = applicationNumber;
//        this.category = category;
//        this.centreCode = centreCode;
//        this.centreName = centreName;
//        this.city = city;
//        this.dateOfExam = dateOfExam;
//        this.dob = dob;
//        this.examEndTime = examEndTime;
//        this.examStartTime = examStartTime;
//        this.imageString = imageString;
//        this.labAllotted = labAllotted;
//        this.name = name;
//        this.occurrance = occurrance;
//        this.reportingTime = reportingTime;
//        this.rollNumber = rollNumber;
//        this.seatAllotted = seatAllotted;
//        this.shift = shift;
//        this.state = state;
//    }

    public CandidatesList(@NonNull String candidateID, String applicationNumber, String category, String centreCode, String centreName, String city, String dateOfExam, String dob, String examEndTime, String examStartTime, String imageString, String labAllotted, String name, Integer occurrance, String reportingTime, String rollNumber, String seatAllotted, String shift, String state, String faceImageString, String irisImageString, String faceStatus, String irisStatus) {
        this.candidateID = candidateID;
        this.applicationNumber = applicationNumber;
        this.category = category;
        this.centreCode = centreCode;
        this.centreName = centreName;
        this.city = city;
        this.dateOfExam = dateOfExam;
        this.dob = dob;
        this.examEndTime = examEndTime;
        this.examStartTime = examStartTime;
        this.imageString = imageString;
        this.labAllotted = labAllotted;
        this.name = name;
        this.occurrance = occurrance;
        this.reportingTime = reportingTime;
        this.rollNumber = rollNumber;
        this.seatAllotted = seatAllotted;
        this.shift = shift;
        this.state = state;
        this.faceImageString = faceImageString;
        this.irisImageString = irisImageString;
        this.faceStatus = faceStatus;
        this.irisStatus = irisStatus;
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

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
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

    public String getFaceImageString() {
        return faceImageString;
    }

    public void setFaceImageString(String faceImageString) {
        this.faceImageString = faceImageString;
    }

    public String getIrisImageString() {
        return irisImageString;
    }

    public void setIrisImageString(String irisImageString) {
        this.irisImageString = irisImageString;
    }

    public String getFaceStatus() {
        return faceStatus;
    }

    public void setFaceStatus(String faceStatus) {
        this.faceStatus = faceStatus;
    }

    public String getIrisStatus() {
        return irisStatus;
    }

    public void setIrisStatus(String irisStatus) {
        this.irisStatus = irisStatus;
    }

}