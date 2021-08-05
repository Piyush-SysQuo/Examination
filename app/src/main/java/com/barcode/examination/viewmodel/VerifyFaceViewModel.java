package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.VerifyFaceResponse.VerifyFaceAPIResponse;
import com.barcode.examination.repository.VerifyFaceRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VerifyFaceViewModel extends AndroidViewModel {

    private LiveData<VerifyFaceAPIResponse> verifyFaceAPIResponseLiveData;
    private VerifyFaceRepository verifyFaceRepository;

    public VerifyFaceViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        verifyFaceRepository = new VerifyFaceRepository();
        verifyFaceAPIResponseLiveData = verifyFaceRepository.verifyFaceAPIResponseLiveData();
    }

    public void verifyFaceApi(@NonNull String token, @NonNull RequestBody rollNumber, MultipartBody.Part part) {
        verifyFaceRepository.verifyCandidateFaceApi(token,rollNumber,part);
    }

    public LiveData<VerifyFaceAPIResponse> getVolumesResponseLiveData() {
        return verifyFaceAPIResponseLiveData;
    }
}
