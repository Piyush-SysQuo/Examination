package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.LogoutResponse.LogoutAPIResponse;
import com.barcode.examination.repository.LogoutRepository;

public class LogoutViewModel extends AndroidViewModel {

    private LiveData<LogoutAPIResponse> logOutResponseMutableLiveData;
    private LogoutRepository logoutRepository;

    public LogoutViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        logoutRepository = new LogoutRepository();
        logOutResponseMutableLiveData = logoutRepository.logOutResponseLiveData();
    }

    public void userLogout(@NonNull String token) {
        logoutRepository.callLogoutApi(token);
    }

    public LiveData<LogoutAPIResponse> getVolumesResponseLiveData() {
        return logOutResponseMutableLiveData;
    }
}
