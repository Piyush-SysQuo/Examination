package com.barcode.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barcode.examination.connection.ApiDataService;
import com.barcode.examination.connection.RetrofitInstance;
import com.barcode.examination.model.RegisterFaceResponse.RegisterFaceAPIResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFaceRepository {

    private MutableLiveData<RegisterFaceAPIResponse> mutableLiveData;


    public RegisterFaceRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call register candidate face api
    public void registerCandidateFaceApi(@NonNull String token, @NonNull RequestBody rollNumber, MultipartBody.Part part){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<RegisterFaceAPIResponse> call = apiDataService.RegisterCandidateFace(token,rollNumber,part);
        call.enqueue(new Callback<RegisterFaceAPIResponse>() {
            @Override
            public void onResponse(Call<RegisterFaceAPIResponse> call, Response<RegisterFaceAPIResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<RegisterFaceAPIResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<RegisterFaceAPIResponse> registerFaceAPIResponseLiveData() {
        return mutableLiveData;
    }

}
