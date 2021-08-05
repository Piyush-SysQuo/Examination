package com.barcode.examination.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.ScanWithFaceResponse.ScanWithFaceAPIResponse;
import com.barcode.examination.repository.ScanWithFaceRepository;

import okhttp3.MultipartBody;

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
