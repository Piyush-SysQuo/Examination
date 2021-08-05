package com.barcode.examination.database.dbmodel;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.barcode.examination.util.Constants;

@Entity
public class StudentDetailsEntity {

    /*Student details*/
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = Constants.KEY_ROLL_NO)
    private String rollNo;

    @ColumnInfo(name = Constants.KEY_NAME)
    private String name;

    @ColumnInfo(name = Constants.KEY_LAB_NO)
    private String labNo;

    @ColumnInfo(name = Constants.KEY_SEAT_NO)
    private String seatNo;

    @ColumnInfo(name = Constants.KEY_CENTER_NAME)
    private String centerName;

    @ColumnInfo(name = Constants.KEY_EXAM_TIME)
    private String examTime;

    @ColumnInfo(name = Constants.KEY_OCCURRENCE)
    private String occurrence;

    @ColumnInfo(name = Constants.KEY_APPLICATION_NO)
    private String applicationNo;

    @ColumnInfo(name = Constants.KEY_REPORTING_TIME)
    private String reportTime;

    @ColumnInfo(name = Constants.KEY_CATEGORY)
    private String category;

    @ColumnInfo(name = Constants.KEY_DOB)
    private String dob;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;


    public StudentDetailsEntity(@NonNull String rollNo, String name,byte[] image) {
        this.rollNo = rollNo;
        this.name = name;
        this.image = image;
    }

    @NonNull
    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(@NonNull String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabNo() {
        return labNo;
    }

    public void setLabNo(String labNo) {
        this.labNo = labNo;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(String occurrence) {
        this.occurrence = occurrence;
    }

    public String getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(String applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
