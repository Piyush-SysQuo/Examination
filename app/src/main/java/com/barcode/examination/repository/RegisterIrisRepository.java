package com.barcode.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barcode.examination.connection.ApiDataService;
import com.barcode.examination.connection.RetrofitInstance;
import com.barcode.examination.model.RegisterIrisResponse.RegisterIrisAPIResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterIrisRepository {

    private MutableLiveData<RegisterIrisAPIResponse> mutableLiveData;


    public RegisterIrisRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call register candidate iris api
    public void registerCandidateIrisApi(@NonNull String token, @NonNull RequestBody rollNumber, MultipartBody.Part part, MultipartBody.Part part2){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<RegisterIrisAPIResponse> call = apiDataService.RegisterCandidateIris(token,rollNumber,part,part2);
        call.enqueue(new Callback<RegisterIrisAPIResponse>() {
            @Override
            public void onResponse(Call<RegisterIrisAPIResponse> call, Response<RegisterIrisAPIResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<RegisterIrisAPIResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<RegisterIrisAPIResponse> registerIrisAPIResponseLiveData() {
        return mutableLiveData;
    }

}
