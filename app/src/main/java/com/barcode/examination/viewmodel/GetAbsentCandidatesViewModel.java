package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.GetAbsentCandidatesResponse.GetAbsentCandidatesAPIResponse;
import com.barcode.examination.repository.GetAbsentCandidatesRepository;

public class GetAbsentCandidatesViewModel extends AndroidViewModel {

    private LiveData<GetAbsentCandidatesAPIResponse> getAbsentCandidatesResponseMutableLiveData;
    private GetAbsentCandidatesRepository getAbsentCandidatesRepository;

    public GetAbsentCandidatesViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        getAbsentCandidatesRepository = new GetAbsentCandidatesRepository();
        getAbsentCandidatesResponseMutableLiveData = getAbsentCandidatesRepository.getAbsentCandidatesResponseLiveData();
    }

    public void getAbsentCandidates(@NonNull String token,String page, String limit) {
        getAbsentCandidatesRepository.callGetAbsentCandidatesAPI(token,page,limit);
    }

    public LiveData<GetAbsentCandidatesAPIResponse> getVolumesResponseLiveData() {
        return getAbsentCandidatesResponseMutableLiveData;
    }
}
