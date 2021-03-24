package com.inspection.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inspection.examination.connection.ApiDataService;
import com.inspection.examination.connection.RetrofitInstance;
import com.inspection.examination.model.GetAbsentCandidatesResponse.GetAbsentCandidatesAPIResponse;
import com.inspection.examination.model.GetCandidateDataResponse.GetCandidateDataAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCandidateDataRepository {

    private MutableLiveData<GetCandidateDataAPIResponse> getCandidateDataResponseMutableLiveData;


    public GetCandidateDataRepository() {

        getCandidateDataResponseMutableLiveData = new MutableLiveData<>();
    }

    // call getCandidateData data api
    public void callGetCandidateDataAPI(@NonNull String token,String rollNumber){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<GetCandidateDataAPIResponse> call = apiDataService.GetCandidateData(token,rollNumber);
        call.enqueue(new Callback<GetCandidateDataAPIResponse>() {
            @Override
            public void onResponse(Call<GetCandidateDataAPIResponse> call, Response<GetCandidateDataAPIResponse> response) {
                if (response.body() != null) {
                    getCandidateDataResponseMutableLiveData.postValue(response.body());
                }else{
                    getCandidateDataResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetCandidateDataAPIResponse> call, Throwable t) {
                getCandidateDataResponseMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<GetCandidateDataAPIResponse> getCandidateDataResponseLiveData() {
        return getCandidateDataResponseMutableLiveData;
    }

}
