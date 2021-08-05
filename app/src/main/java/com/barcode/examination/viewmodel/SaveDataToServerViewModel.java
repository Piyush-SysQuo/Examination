package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.DataToServer;
import com.barcode.examination.model.SaveDataToServerResponse.SaveDataToServerAPIResponse;
import com.barcode.examination.repository.SaveDataToServerRepository;

import java.util.List;

public class SaveDataToServerViewModel extends AndroidViewModel {

    private LiveData<SaveDataToServerAPIResponse> saveDataToServerResponseMutableLiveData;
    private SaveDataToServerRepository saveDataToServerRepository;

    public SaveDataToServerViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        saveDataToServerRepository = new SaveDataToServerRepository();
        saveDataToServerResponseMutableLiveData = saveDataToServerRepository.saveDataToServerResponseLiveData();
    }

    public void saveDataToServer(@NonNull String token, @NonNull List<DataToServer> list) {
        saveDataToServerRepository.callSaveDataToServerAPI(token, list);
    }

    public LiveData<SaveDataToServerAPIResponse> getVolumesResponseLiveData() {
        return saveDataToServerResponseMutableLiveData;
    }
}
