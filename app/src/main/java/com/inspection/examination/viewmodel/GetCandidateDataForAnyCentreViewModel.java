package com.inspection.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.inspection.examination.model.GetCandidateDataForAnyCentreResponse.GetCandidateDataForAnyCentreAPIResponse;
import com.inspection.examination.model.GetCandidateDataResponse.GetCandidateDataAPIResponse;
import com.inspection.examination.repository.GetCandidateDataForAnyCentreRepository;
import com.inspection.examination.repository.GetCandidateDataRepository;

public class GetCandidateDataForAnyCentreViewModel extends AndroidViewModel {

    private LiveData<GetCandidateDataForAnyCentreAPIResponse> getCandidateDataForAnyCentreResponseMutableLiveData;
    private GetCandidateDataForAnyCentreRepository getCandidateDataForAnyCentreRepository;

    public GetCandidateDataForAnyCentreViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        getCandidateDataForAnyCentreRepository = new GetCandidateDataForAnyCentreRepository();
        getCandidateDataForAnyCentreResponseMutableLiveData = getCandidateDataForAnyCentreRepository.getCandidateDataForAnyCentreResponseLiveData();
    }

    public void getCandidateDataForAnyCentre(@NonNull String token,String rollNumber) {
        getCandidateDataForAnyCentreRepository.callGetCandidateDataForAnyCentreAPI(token,rollNumber);
    }

    public LiveData<GetCandidateDataForAnyCentreAPIResponse> getVolumesResponseLiveData() {
        return getCandidateDataForAnyCentreResponseMutableLiveData;
    }
}
