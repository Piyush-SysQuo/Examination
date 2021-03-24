package com.inspection.examination.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentAvailabilityList {

    @SerializedName("Roll_No")
    @Expose
    private String rollNo;

    @SerializedName("Name_Candidate")
    @Expose
    private String name;

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
