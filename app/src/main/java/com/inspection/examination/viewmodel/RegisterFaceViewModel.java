package com.inspection.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.inspection.examination.model.BypassAuthResponse.BypassAuthAPIResponse;
import com.inspection.examination.model.RegisterFaceResponse.RegisterFaceAPIResponse;
import com.inspection.examination.repository.BypassAuthRepository;
import com.inspection.examination.repository.RegisterFaceRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterFaceViewModel extends AndroidViewModel {

    private LiveData<RegisterFaceAPIResponse> registerFaceAPIResponseLiveData;
    private RegisterFaceRepository registerFaceRepository;

    public RegisterFaceViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        registerFaceRepository = new RegisterFaceRepository();
        registerFaceAPIResponseLiveData = registerFaceRepository.registerFaceAPIResponseLiveData();
    }

    public void registerFaceApi(@NonNull String token, @NonNull RequestBody rollNumber, MultipartBody.Part part) {
        registerFaceRepository.registerCandidateFaceApi(token,rollNumber,part);
    }

    public LiveData<RegisterFaceAPIResponse> getVolumesResponseLiveData() {
        return registerFaceAPIResponseLiveData;
    }
}
