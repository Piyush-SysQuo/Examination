package com.barcode.examination.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.barcode.examination.model.DataToServer;
import com.barcode.examination.model.SaveDataToServerResp;
import com.barcode.examination.repository.SaveDataToServerRepo;
import java.util.List;

public class SaveDataToServerVm extends AndroidViewModel {

    private LiveData<SaveDataToServerResp> saveDataToServerRespLiveData;
    private SaveDataToServerRepo dataToServerRepo;

    public SaveDataToServerVm(@NonNull Application application) {
        super(application);
    }

    public void init() {
        dataToServerRepo = new SaveDataToServerRepo();
        saveDataToServerRespLiveData = dataToServerRepo.dataToServerLiveData();
    }

    public void saveData(List<DataToServer> list) {
        dataToServerRepo.callApi(list);
    }

    public LiveData<SaveDataToServerResp> getVolumesResponseLiveData() {
        return saveDataToServerRespLiveData;
    }
}
