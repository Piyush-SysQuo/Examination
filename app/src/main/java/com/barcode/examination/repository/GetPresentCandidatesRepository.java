package com.barcode.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barcode.examination.connection.ApiDataService;
import com.barcode.examination.connection.RetrofitInstance;
import com.barcode.examination.model.GetPresentCandidatesResponse.GetPresentCandidatesAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPresentCandidatesRepository {

    private MutableLiveData<GetPresentCandidatesAPIResponse> getPresentCandidatesResponseMutableLiveData;


    public GetPresentCandidatesRepository() {

        getPresentCandidatesResponseMutableLiveData = new MutableLiveData<>();
    }

    // call sync data api
    public void callGetPresentCandidatesAPI(@NonNull String token,String page, String limit){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<GetPresentCandidatesAPIResponse> call = apiDataService.GetPresentCandidatesList(token,page,limit);
        call.enqueue(new Callback<GetPresentCandidatesAPIResponse>() {
            @Override
            public void onResponse(Call<GetPresentCandidatesAPIResponse> call, Response<GetPresentCandidatesAPIResponse> response) {
                if (response.body() != null) {
                    getPresentCandidatesResponseMutableLiveData.postValue(response.body());
                }else{
                    getPresentCandidatesResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetPresentCandidatesAPIResponse> call, Throwable t) {
                getPresentCandidatesResponseMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<GetPresentCandidatesAPIResponse> getPresentCandidatesResponseLiveData() {
        return getPresentCandidatesResponseMutableLiveData;
    }

}
