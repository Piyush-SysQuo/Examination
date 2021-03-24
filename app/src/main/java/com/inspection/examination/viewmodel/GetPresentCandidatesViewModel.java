package com.inspection.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.inspection.examination.model.GetPresentCandidatesResponse.GetPresentCandidatesAPIResponse;
import com.inspection.examination.model.SyncDataResponse.SyncDataAPIResponse;
import com.inspection.examination.repository.GetPresentCandidatesRepository;
import com.inspection.examination.repository.SyncDataRepository;

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
