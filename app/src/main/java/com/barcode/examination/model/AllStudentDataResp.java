
package com.barcode.examination.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllStudentDataResp {

    @SerializedName("list_student")
    @Expose
    private List<ListStudent> listStudent = null;

    public List<ListStudent> getListStudent() {
        return listStudent;
    }

    public void setListStudent(List<ListStudent> listStudent) {
        this.listStudent = listStudent;
    }

}
