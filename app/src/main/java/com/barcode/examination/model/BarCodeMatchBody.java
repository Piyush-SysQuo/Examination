package com.barcode.examination.model;

public class BarCodeMatchBody {

    private String rollno;

    public BarCodeMatchBody(String rollno) {

        this.rollno = rollno;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }
}
