package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.barcode.examination.model.AllStudentDataResp;
import com.barcode.examination.repository.GetAllStdDataRepo;

public class GetAllStudentViewModel extends AndroidViewModel {

    private LiveData<AllStudentDataResp> allStudentDataRespLiveData;
    private GetAllStdDataRepo getAllStdDataRepo;

    public GetAllStudentViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        getAllStdDataRepo = new GetAllStdDataRepo();
        allStudentDataRespLiveData = getAllStdDataRepo.allStudentDataRespLiveData();
    }

    public void getStudentList() {
        getAllStdDataRepo.calGetAllStudentListApi();
    }

    public LiveData<AllStudentDataResp> getVolumesResponseLiveData() {
        return allStudentDataRespLiveData;
    }
}
