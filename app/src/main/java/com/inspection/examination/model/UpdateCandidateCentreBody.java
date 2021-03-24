package com.inspection.examination.model;

public class UpdateCandidateCentreBody {

    private String rollNumber;

    public UpdateCandidateCentreBody(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }


}
