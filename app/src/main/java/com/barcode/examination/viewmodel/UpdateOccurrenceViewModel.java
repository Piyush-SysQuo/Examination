package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.UpdateOccurrenceResponse.UpdateOccurrenceAPIResponse;
import com.barcode.examination.repository.UpdateOccurrenceRepository;

public class UpdateOccurrenceViewModel extends AndroidViewModel {

    private LiveData<UpdateOccurrenceAPIResponse> updateOccurrenceResponseMutableLiveData;
    private UpdateOccurrenceRepository updateOccurrenceRepository;

    public UpdateOccurrenceViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        updateOccurrenceRepository = new UpdateOccurrenceRepository();
        updateOccurrenceResponseMutableLiveData = updateOccurrenceRepository.updateOccurrenceResponseLiveData();
    }

    public void updateOccurrence(@NonNull String token, @NonNull String rollNumber) {
        updateOccurrenceRepository.callUpdateOccurrenceApi(token, rollNumber);
    }

    public LiveData<UpdateOccurrenceAPIResponse> getVolumesResponseLiveData() {
        return updateOccurrenceResponseMutableLiveData;
    }
}
