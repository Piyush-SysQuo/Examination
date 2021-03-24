package com.inspection.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.inspection.examination.model.ScanWithFaceResponse.ScanWithFaceAPIResponse;
import com.inspection.examination.model.VerifyFaceResponse.VerifyFaceAPIResponse;
import com.inspection.examination.repository.ScanWithFaceRepository;
import com.inspection.examination.repository.VerifyFaceRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ScanWithFaceViewModel extends AndroidViewModel {

    private LiveData<ScanWithFaceAPIResponse> scanWithFaceAPIResponseLiveData;
    private ScanWithFaceRepository scanWithFaceRepository;

    public ScanWithFaceViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        scanWithFaceRepository = new ScanWithFaceRepository();
        scanWithFaceAPIResponseLiveData = scanWithFaceRepository.scanFaceForDataAPIResponseLiveData();
    }

    public void scanFaceForDataApi(@NonNull String token, MultipartBody.Part part) {
        scanWithFaceRepository.scanFaceForData(token,part);
    }

    public LiveData<ScanWithFaceAPIResponse> getVolumesResponseLiveData() {
        return scanWithFaceAPIResponseLiveData;
    }
}
