package com.inspection.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inspection.examination.connection.ApiDataService;
import com.inspection.examination.connection.RetrofitInstance;
import com.inspection.examination.model.BypassAuthResponse.BypassAuthAPIResponse;
import com.inspection.examination.model.UpdateOccurrenceBody;
import com.inspection.examination.model.UpdateOccurrenceResponse.UpdateOccurrenceAPIResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BypassAuthRepository {

    private MutableLiveData<BypassAuthAPIResponse> mutableLiveData;


    public BypassAuthRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call bypass candidate auth api
    public void bypassCandidateAuthApi(@NonNull String token, @NonNull RequestBody rollNumber, @NonNull RequestBody authType, @NonNull RequestBody reason, MultipartBody.Part part){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<BypassAuthAPIResponse> call = apiDataService.BypassCandidateAuth(token,rollNumber,authType,reason,part);
        call.enqueue(new Callback<BypassAuthAPIResponse>() {
            @Override
            public void onResponse(Call<BypassAuthAPIResponse> call, Response<BypassAuthAPIResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<BypassAuthAPIResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<BypassAuthAPIResponse> bypassAuthAPIResponseLiveData() {
        return mutableLiveData;
    }

}
