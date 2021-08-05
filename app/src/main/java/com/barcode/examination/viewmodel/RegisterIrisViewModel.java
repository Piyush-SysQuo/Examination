package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.RegisterIrisResponse.RegisterIrisAPIResponse;
import com.barcode.examination.repository.RegisterIrisRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterIrisViewModel extends AndroidViewModel {

    private LiveData<RegisterIrisAPIResponse> registerIrisAPIResponseLiveData;
    private RegisterIrisRepository registerIrisRepository;

    public RegisterIrisViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        registerIrisRepository = new RegisterIrisRepository();
        registerIrisAPIResponseLiveData = registerIrisRepository.registerIrisAPIResponseLiveData();
    }

    public void registerIrisApi(@NonNull String token, @NonNull RequestBody rollNumber, MultipartBody.Part part, MultipartBody.Part part2) {
        registerIrisRepository.registerCandidateIrisApi(token,rollNumber,part,part2);
    }

    public LiveData<RegisterIrisAPIResponse> getVolumesResponseLiveData() {
        return registerIrisAPIResponseLiveData;
    }
}
