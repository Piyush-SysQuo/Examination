package com.inspection.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.inspection.examination.connection.ApiDataService;
import com.inspection.examination.connection.RetrofitInstance;
import com.inspection.examination.model.LoginBody;
import com.inspection.examination.model.LoginInResponse;
import com.inspection.examination.model.LoginResponse.LoginAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private MutableLiveData<LoginAPIResponse> logInResponseMutableLiveData;


    public LoginRepository() {

        logInResponseMutableLiveData = new MutableLiveData<>();
    }

    // call login api
    public void callLoginApi(@NonNull String userName ,@NonNull String password){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<LoginAPIResponse> call = apiDataService.GetValidLogin(new LoginBody(userName,password));
        call.enqueue(new Callback<LoginAPIResponse>() {
            @Override
            public void onResponse(Call<LoginAPIResponse> call, Response<LoginAPIResponse> response) {
                if (response.body() != null) {
                    logInResponseMutableLiveData.postValue(response.body());
                }else{
                    logInResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<LoginAPIResponse> call, Throwable t) {
                logInResponseMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<LoginAPIResponse> logInResponseLiveData() {
        return logInResponseMutableLiveData;
    }

}
