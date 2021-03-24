package com.inspection.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.inspection.examination.model.GetAbsentCandidatesResponse.GetAbsentCandidatesAPIResponse;
import com.inspection.examination.model.GetPresentCandidatesResponse.GetPresentCandidatesAPIResponse;
import com.inspection.examination.repository.GetAbsentCandidatesRepository;
import com.inspection.examination.repository.GetPresentCandidatesRepository;

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
