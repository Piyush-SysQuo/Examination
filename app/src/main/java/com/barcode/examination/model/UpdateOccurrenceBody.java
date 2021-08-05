package com.barcode.examination.model;

public class UpdateOccurrenceBody {

    private String rollNumber;

    public UpdateOccurrenceBody(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }


}
