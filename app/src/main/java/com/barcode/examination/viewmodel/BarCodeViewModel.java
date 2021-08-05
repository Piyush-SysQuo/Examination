package com.barcode.examination.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.barcode.examination.model.BarcodeResponse;
import com.barcode.examination.repository.BarCodeRepository;


public class BarCodeViewModel  extends AndroidViewModel {

    private LiveData<BarcodeResponse> barcodeResponseLiveData;
    private BarCodeRepository barCodeRepository;

    public BarCodeViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        barCodeRepository = new BarCodeRepository();
        barcodeResponseLiveData = barCodeRepository.barCodeResponseLiveData();
    }

    public void barCodeScan(@NonNull String rollNumber) {
        barCodeRepository.callBarcodeApi(rollNumber);
    }

    public LiveData<BarcodeResponse> getVolumesResponseLiveData() {
        return barcodeResponseLiveData;
    }
}