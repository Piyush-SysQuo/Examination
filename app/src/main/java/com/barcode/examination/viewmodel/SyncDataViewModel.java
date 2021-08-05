package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.SyncDataResponse.SyncDataAPIResponse;
import com.barcode.examination.repository.SyncDataRepository;

public class SyncDataViewModel extends AndroidViewModel {

    private LiveData<SyncDataAPIResponse> syncDataResponseMutableLiveData;
    private SyncDataRepository syncDataRepository;

    public SyncDataViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        syncDataRepository = new SyncDataRepository();
        syncDataResponseMutableLiveData = syncDataRepository.syncDataResponseLiveData();
    }

    public void syncData(@NonNull String token) {
        syncDataRepository.callSyncDataAPI(token);
    }

    public LiveData<SyncDataAPIResponse> getVolumesResponseLiveData() {
        return syncDataResponseMutableLiveData;
    }
}
