package com.inspection.examination.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.inspection.examination.model.StudentAvailabilityList;
import com.inspection.examination.model.StudentAvailabilityResp;
import com.inspection.examination.repository.AbsentStudentRepo;
import com.inspection.examination.repository.PresentStudentRepo;

public class PresentStudentVm extends AndroidViewModel {

    private LiveData<StudentAvailabilityResp> responseLiveData;
    private PresentStudentRepo presentStudentRepo;

    public PresentStudentVm(@NonNull Application application) {
        super(application);
    }

    public void init() {
        presentStudentRepo = new PresentStudentRepo();
        responseLiveData = presentStudentRepo.data();
    }

    public void getPresentStudent(int pages, int limit) {
        presentStudentRepo.getPresentData(pages, limit);
    }

    public LiveData<StudentAvailabilityResp> getVolumesResponseLiveData() {
        return responseLiveData;
    }
}
