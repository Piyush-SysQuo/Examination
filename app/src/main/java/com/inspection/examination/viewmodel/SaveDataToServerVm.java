package com.inspection.examination.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.inspection.examination.model.DataToServer;
import com.inspection.examination.model.SaveDataToServerResp;
import com.inspection.examination.repository.SaveDataToServerRepo;
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
