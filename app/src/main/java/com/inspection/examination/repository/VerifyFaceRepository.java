package com.inspection.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inspection.examination.connection.ApiDataService;
import com.inspection.examination.connection.RetrofitInstance;
import com.inspection.examination.model.RegisterFaceResponse.RegisterFaceAPIResponse;
import com.inspection.examination.model.VerifyFaceResponse.VerifyFaceAPIResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyFaceRepository {

    private MutableLiveData<VerifyFaceAPIResponse> mutableLiveData;


    public VerifyFaceRepository() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call verify candidate face api
    public void verifyCandidateFaceApi(@NonNull String token, @NonNull RequestBody rollNumber, MultipartBody.Part part){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<VerifyFaceAPIResponse> call = apiDataService.VerifyCandidateFace(token,rollNumber,part);
        call.enqueue(new Callback<VerifyFaceAPIResponse>() {
            @Override
            public void onResponse(Call<VerifyFaceAPIResponse> call, Response<VerifyFaceAPIResponse> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<VerifyFaceAPIResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<VerifyFaceAPIResponse> verifyFaceAPIResponseLiveData() {
        return mutableLiveData;
    }

}
