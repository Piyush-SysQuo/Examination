
package com.inspection.examination.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.inspection.examination.util.Constants;

import org.parceler.Parcel;

@Parcel
@Entity
public class ListStudent {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = Constants.KEY_ROLL_NO)
    @SerializedName("Roll_No")
    @Expose
    private String rollNo;


    @ColumnInfo(name = Constants.KEY_APPLICATION_NO)
    @SerializedName("Application_No")
    @Expose
    private String applicationNo;

    @ColumnInfo(name = Constants.KEY_CATEGORY)
    @SerializedName("Category")
    @Expose
    private String category;

    @ColumnInfo(name = Constants.KEY_CENTER_NAME)
    @SerializedName("Centre_Name")
    @Expose
    private String centreName;


    @SerializedName("Date_Day_of_Exam")
    @Expose
    private String dateDayOfExam;

    @ColumnInfo(name = Constants.KEY_DOB)
    @SerializedName("Date_of_Birth")
    @Expose
    private String dateOfBirth;

    @ColumnInfo(name = Constants.KEY_EXAM_TIME)
    @SerializedName("Exam_Time")
    @Expose
    private String examTime;

    @ColumnInfo(name = Constants.KEY_LAB_NO)
    @SerializedName("Lab_No")
    @Expose
    private String labNo;

    @ColumnInfo(name = Constants.KEY_NAME)
    @SerializedName("Name_Candidate")
    @Expose
    private String nameCandidate;

    @ColumnInfo(name = Constants.KEY_REPORTING_TIME)
    @SerializedName("Reporting_Session")
    @Expose
    private String reportingSession;

    @ColumnInfo(name = Constants.KEY_SEAT_NO)
    @SerializedName("Seat_No")
    @Expose
    private String seatNo;

    @SerializedName("id")
    @Expose
    private long id;

    @ColumnInfo(name = Constants.KEY_IMAGE_URL)
    @SerializedName("img")
    @Expose
    private String img;

    @ColumnInfo(name = Constants.KEY_OCCURRENCE)
    @SerializedName("Times")
    @Expose
    private long times;

    @ColumnInfo(name = Constants.KEY_COUNT)
    private long count;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
