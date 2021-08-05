package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.GetCandidateDataForAnyCentreResponse.GetCandidateDataForAnyCentreAPIResponse;
import com.barcode.examination.repository.GetCandidateDataForAnyCentreRepository;

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
