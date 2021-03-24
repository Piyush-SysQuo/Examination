package com.inspection.examination.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentAvailabilityResp {

    @SerializedName("student_availability")
    @Expose
    private List<StudentAvailabilityList> studentAvailability = null;

    @SerializedName("msg")
    @Expose
    private String msg;

    public List<StudentAvailabilityList> getStudentAvailability() {
        return studentAvailability;
    }

    public void setStudentAvailability(List<StudentAvailabilityList> studentAvailability) {
        this.studentAvailability = studentAvailability;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

