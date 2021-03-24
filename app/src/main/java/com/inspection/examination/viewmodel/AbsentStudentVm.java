package com.inspection.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.inspection.examination.model.StudentAvailabilityList;
import com.inspection.examination.model.StudentAvailabilityResp;
import com.inspection.examination.repository.AbsentStudentRepo;

public class AbsentStudentVm extends AndroidViewModel {

    private LiveData<StudentAvailabilityResp> responseLiveData;
    private AbsentStudentRepo absentStudentRepo;

    public AbsentStudentVm(@NonNull Application application) {
        super(application);
    }

    public void init() {
        absentStudentRepo = new AbsentStudentRepo();
        responseLiveData = absentStudentRepo.data();
    }

    public void getAbsentStudent(int pages, int limit) {
        absentStudentRepo.getAbsentData(pages,limit);
    }

    public LiveData<StudentAvailabilityResp> getVolumesResponseLiveData() {
        return responseLiveData;
    }
}
