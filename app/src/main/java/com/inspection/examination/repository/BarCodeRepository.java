package com.inspection.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inspection.examination.connection.ApiDataService;
import com.inspection.examination.connection.RetrofitInstance;
import com.inspection.examination.model.BarCodeMatchBody;
import com.inspection.examination.model.BarcodeResponse;
import com.inspection.examination.model.LoginBody;
import com.inspection.examination.model.LoginInResponse;

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
