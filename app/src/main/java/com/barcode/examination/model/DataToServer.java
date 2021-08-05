package com.barcode.examination.model;

public class DataToServer  {

    String rollNumber;
    int occurrance;
    String centerCode;

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public int getOccurrance() {
        return occurrance;
    }

    public void setOccurrance(int occurrance) {
        this.occurrance = occurrance;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }
}
