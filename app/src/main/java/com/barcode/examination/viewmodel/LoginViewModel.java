package com.barcode.examination.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.LoginResponse.LoginAPIResponse;
import com.barcode.examination.repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    private LiveData<LoginAPIResponse> logInResponseMutableLiveData;
    private LoginRepository loginRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        loginRepository = new LoginRepository();
        logInResponseMutableLiveData = loginRepository.logInResponseLiveData();
    }

    public void userLogin(@NonNull String userName, @NonNull  String password) {
        loginRepository.callLoginApi(userName ,password);
    }

    public LiveData<LoginAPIResponse> getVolumesResponseLiveData() {
        return logInResponseMutableLiveData;
    }
}
