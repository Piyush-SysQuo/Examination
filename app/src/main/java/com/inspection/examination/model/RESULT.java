
package com.inspection.examination.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class RESULT {

    @SerializedName("Application_No")
    @Expose
    private String applicationNo;
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("Centre_Name")
    @Expose
    private String centreName;
    @SerializedName("Date_Day_of_Exam")
    @Expose
    private String dateDayOfExam;
    @SerializedName("Date_of_Birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("Exam_Time")
    @Expose
    private String examTime;
    @SerializedName("Lab_No")
    @Expose
    private String labNo;
    @SerializedName("Name_Candidate")
    @Expose
    private String nameCandidate;
    @SerializedName("Reporting_Session")
    @Expose
    private String reportingSession;
    @SerializedName("Roll_No")
    @Expose
    private String rollNo;
    @SerializedName("Seat_No")
    @Expose
    private String seatNo;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("times")
    @Expose
    private String times;

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }

    public String getDateDayOfExam() {
        return dateDayOfExam;
    }

    public void setDateDayOfExam(String dateDayOfExam) {
        this.dateDayOfExam = dateDayOfExam;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getLabNo() {
        return labNo;
    }

    public void setLabNo(String labNo) {
        this.labNo = labNo;
    }

    public String getNameCandidate() {
        return nameCandidate;
    }

    public void setNameCandidate(String nameCandidate) {
        this.nameCandidate = nameCandidate;
    }

    public String getReportingSession() {
        return reportingSession;
    }

    public void setReportingSession(String reportingSession) {
        this.reportingSession = reportingSession;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
