package com.inspection.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inspection.examination.connection.ApiDataService;
import com.inspection.examination.connection.RetrofitInstance;
import com.inspection.examination.model.GetCandidateDataForAnyCentreResponse.GetCandidateDataForAnyCentreAPIResponse;
import com.inspection.examination.model.GetCandidateDataResponse.GetCandidateDataAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCandidateDataForAnyCentreRepository {

    private MutableLiveData<GetCandidateDataForAnyCentreAPIResponse> getCandidateDataForAnyCentreResponseMutableLiveData;


    public GetCandidateDataForAnyCentreRepository() {

        getCandidateDataForAnyCentreResponseMutableLiveData = new MutableLiveData<>();
    }

    // call getCandidateData data api
    public void callGetCandidateDataForAnyCentreAPI(@NonNull String token,String rollNumber){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<GetCandidateDataForAnyCentreAPIResponse> call = apiDataService.GetCandidateDataForAnyCentre(token,rollNumber);
        call.enqueue(new Callback<GetCandidateDataForAnyCentreAPIResponse>() {
            @Override
            public void onResponse(Call<GetCandidateDataForAnyCentreAPIResponse> call, Response<GetCandidateDataForAnyCentreAPIResponse> response) {
                if (response.body() != null) {
                    getCandidateDataForAnyCentreResponseMutableLiveData.postValue(response.body());
                }else{
                    getCandidateDataForAnyCentreResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<GetCandidateDataForAnyCentreAPIResponse> call, Throwable t) {
                getCandidateDataForAnyCentreResponseMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<GetCandidateDataForAnyCentreAPIResponse> getCandidateDataForAnyCentreResponseLiveData() {
        return getCandidateDataForAnyCentreResponseMutableLiveData;
    }

}
