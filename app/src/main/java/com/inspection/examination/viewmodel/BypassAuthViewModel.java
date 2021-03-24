package com.inspection.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.inspection.examination.model.BypassAuthResponse.BypassAuthAPIResponse;
import com.inspection.examination.model.UpdateOccurrenceResponse.UpdateOccurrenceAPIResponse;
import com.inspection.examination.repository.BypassAuthRepository;
import com.inspection.examination.repository.UpdateOccurrenceRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BypassAuthViewModel extends AndroidViewModel {

    private LiveData<BypassAuthAPIResponse> bypassAuthAPIResponseLiveData;
    private BypassAuthRepository bypassAuthRepository;

    public BypassAuthViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        bypassAuthRepository = new BypassAuthRepository();
        bypassAuthAPIResponseLiveData = bypassAuthRepository.bypassAuthAPIResponseLiveData();
    }

    public void bypassAuthApi(@NonNull String token, @NonNull RequestBody rollNumber, @NonNull RequestBody authType, @NonNull RequestBody reason, MultipartBody.Part part) {
        bypassAuthRepository.bypassCandidateAuthApi(token,rollNumber,authType,reason,part);
    }

    public LiveData<BypassAuthAPIResponse> getVolumesResponseLiveData() {
        return bypassAuthAPIResponseLiveData;
    }
}
