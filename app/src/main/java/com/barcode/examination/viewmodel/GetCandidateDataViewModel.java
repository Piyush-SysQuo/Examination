package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.GetCandidateDataResponse.GetCandidateDataAPIResponse;
import com.barcode.examination.repository.GetCandidateDataRepository;

public class GetCandidateDataViewModel extends AndroidViewModel {

    private LiveData<GetCandidateDataAPIResponse> getCandidateDataResponseMutableLiveData;
    private GetCandidateDataRepository getCandidateDataRepository;

    public GetCandidateDataViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        getCandidateDataRepository = new GetCandidateDataRepository();
        getCandidateDataResponseMutableLiveData = getCandidateDataRepository.getCandidateDataResponseLiveData();
    }

    public void getCandidateData(@NonNull String token,String rollNumber) {
        getCandidateDataRepository.callGetCandidateDataAPI(token,rollNumber);
    }

    public LiveData<GetCandidateDataAPIResponse> getVolumesResponseLiveData() {
        return getCandidateDataResponseMutableLiveData;
    }
}
