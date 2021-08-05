package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.GetPresentCandidatesResponse.GetPresentCandidatesAPIResponse;
import com.barcode.examination.repository.GetPresentCandidatesRepository;

public class GetPresentCandidatesViewModel extends AndroidViewModel {

    private LiveData<GetPresentCandidatesAPIResponse> getPresentCandidatesResponseMutableLiveData;
    private GetPresentCandidatesRepository getPresentCandidatesRepository;

    public GetPresentCandidatesViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        getPresentCandidatesRepository = new GetPresentCandidatesRepository();
        getPresentCandidatesResponseMutableLiveData = getPresentCandidatesRepository.getPresentCandidatesResponseLiveData();
    }

    public void getPresentCandidates(@NonNull String token,String page, String limit) {
        getPresentCandidatesRepository.callGetPresentCandidatesAPI(token,page,limit);
    }

    public LiveData<GetPresentCandidatesAPIResponse> getVolumesResponseLiveData() {
        return getPresentCandidatesResponseMutableLiveData;
    }
}
