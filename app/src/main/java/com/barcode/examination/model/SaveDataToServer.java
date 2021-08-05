package com.barcode.examination.model;

import java.util.List;

public class SaveDataToServer {

    private List<DataToServer> candidatesData = null;

    public SaveDataToServer(List<DataToServer> candidatesData) {
        this.candidatesData = candidatesData;
    }

    public List<DataToServer> getCandidatesData() {
        return candidatesData;
    }

    public void setCandidatesData(List<DataToServer> candidatesData) {
        this.candidatesData = candidatesData;
    }
}
