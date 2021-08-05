package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.UpdateCandidateCentreResponse.UpdateCandidateCentreAPIResponse;
import com.barcode.examination.repository.UpdateCandidateCentreRepository;

public class UpdateCandidateCentreViewModel extends AndroidViewModel {

    private LiveData<UpdateCandidateCentreAPIResponse> updateCandidateCentreResponseMutableLiveData;
    private UpdateCandidateCentreRepository updateCandidateCentreRepository;

    public UpdateCandidateCentreViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        updateCandidateCentreRepository = new UpdateCandidateCentreRepository();
        updateCandidateCentreResponseMutableLiveData = updateCandidateCentreRepository.updateCandidateCentreResponseLiveData();
    }

    public void updateCandidateCentre(@NonNull String token, @NonNull String rollNumber) {
        updateCandidateCentreRepository.callUpdateCandidateCentreApi(token, rollNumber);
    }

    public LiveData<UpdateCandidateCentreAPIResponse> getVolumesResponseLiveData() {
        return updateCandidateCentreResponseMutableLiveData;
    }
}
