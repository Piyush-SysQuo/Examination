
package com.inspection.examination.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BarcodeResponse {

    @SerializedName("RESULT")
    @Expose
    private RESULT rESULT;
    @SerializedName("msg")
    @Expose
    private String msg;

    public RESULT getRESULT() {
        return rESULT;
    }

    public void setRESULT(RESULT rESULT) {
        this.rESULT = rESULT;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
