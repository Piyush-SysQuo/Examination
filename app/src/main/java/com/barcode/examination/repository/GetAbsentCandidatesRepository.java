package com.barcode.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barcode.examination.connection.ApiDataService;
import com.barcode.examination.connection.RetrofitInstance;
import com.barcode.examination.model.GetAbsentCandidatesResponse.GetAbsentCandidatesAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAbsentCandidatesRepository {

    private MutableLiveData<GetAbsentCandidatesAPIResponse> getAbsentCandidatesResponseMutableLiveData;


    public GetAbsentCandidatesRepository() {

        getAbsentCandidatesResponseMutableLiveData = new MutableLiveData<>();
    }

    // call sync data api
    public void callGetAbsentCandidatesAPI(@NonNull String token,String page, String limit){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<GetAbsentCandidatesAPIResponse> call = apiDataService.GetAbsentCandidatesList(token,page,limit);
        call.enqueue(new Callback<GetAbsentCandidatesAPIResponse>() {
            @Override
            public void onResponse(Call<GetAbsentCandidatesAPIResponse> call, Response<GetAbsentCandidatesAPIResponse> response) {
                if (response.body() != null) {
                    getAbsentCandidatesResponseMutableLiveData.postValue(response.body());
                }else{
                    getAbsentCandidatesResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetAbsentCandidatesAPIResponse> call, Throwable t) {
                getAbsentCandidatesResponseMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<GetAbsentCandidatesAPIResponse> getAbsentCandidatesResponseLiveData() {
        return getAbsentCandidatesResponseMutableLiveData;
    }

}
