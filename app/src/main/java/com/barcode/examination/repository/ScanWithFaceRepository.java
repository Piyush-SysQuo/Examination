package com.barcode.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barcode.examination.connection.ApiDataService;
import com.barcode.examination.connection.RetrofitInstance;
import com.barcode.examination.model.ScanWithFaceResponse.ScanWithFaceAPIResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanWithFaceRepository {

    private MutableLiveData<ScanWithFaceAPIResponse> mutableLiveData;


    public ScanWithFaceRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call scan with face candidate api
    public void scanFaceForData(@NonNull String token, MultipartBody.Part part){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<ScanWithFaceAPIResponse> call = apiDataService.ScanFaceForData(token,part);
        call.enqueue(new Callback<ScanWithFaceAPIResponse>() {
            @Override
            public void onResponse(Call<ScanWithFaceAPIResponse> call, Response<ScanWithFaceAPIResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ScanWithFaceAPIResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<ScanWithFaceAPIResponse> scanFaceForDataAPIResponseLiveData() {
        return mutableLiveData;
    }

}
