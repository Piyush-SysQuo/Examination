package com.barcode.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barcode.examination.connection.ApiDataService;
import com.barcode.examination.connection.RetrofitInstance;
import com.barcode.examination.model.BarCodeMatchBody;
import com.barcode.examination.model.BarcodeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarCodeRepository {

    private MutableLiveData<BarcodeResponse> barcodeResponseMutableLiveData;


    public BarCodeRepository() {

        barcodeResponseMutableLiveData = new MutableLiveData<>();
    }

    // call barCode api
    public void callBarcodeApi(@NonNull String rollNumber){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<BarcodeResponse> call = apiDataService.barCodeMatch(new BarCodeMatchBody(rollNumber));
        call.enqueue(new Callback<BarcodeResponse>() {
            @Override
            public void onResponse(Call<BarcodeResponse> call, Response<BarcodeResponse> response) {
                if (response.body() != null) {
                    barcodeResponseMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BarcodeResponse> call, Throwable t) {
                barcodeResponseMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<BarcodeResponse> barCodeResponseLiveData() {
        return barcodeResponseMutableLiveData;
    }

}
