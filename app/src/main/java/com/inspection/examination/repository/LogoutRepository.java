package com.inspection.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inspection.examination.connection.ApiDataService;
import com.inspection.examination.connection.RetrofitInstance;
import com.inspection.examination.model.LogoutResponse.LogoutAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutRepository {

    private MutableLiveData<LogoutAPIResponse> logOutResponseMutableLiveData;


    public LogoutRepository() {

        logOutResponseMutableLiveData = new MutableLiveData<>();
    }

    // call login api
    public void callLogoutApi(@NonNull String token){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<LogoutAPIResponse> call = apiDataService.Logout(token);
        call.enqueue(new Callback<LogoutAPIResponse>() {
            @Override
            public void onResponse(Call<LogoutAPIResponse> call, Response<LogoutAPIResponse> response) {
                if (response.body() != null) {
                    logOutResponseMutableLiveData.postValue(response.body());
                }else{
                    logOutResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<LogoutAPIResponse> call, Throwable t) {
                logOutResponseMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<LogoutAPIResponse> logOutResponseLiveData() {
        return logOutResponseMutableLiveData;
    }

}
