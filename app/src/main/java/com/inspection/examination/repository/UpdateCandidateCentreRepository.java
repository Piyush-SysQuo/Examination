package com.inspection.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inspection.examination.connection.ApiDataService;
import com.inspection.examination.connection.RetrofitInstance;
import com.inspection.examination.model.UpdateCandidateCentreBody;
import com.inspection.examination.model.UpdateCandidateCentreResponse.UpdateCandidateCentreAPIResponse;
import com.inspection.examination.model.UpdateOccurrenceBody;
import com.inspection.examination.model.UpdateOccurrenceResponse.UpdateOccurrenceAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCandidateCentreRepository {

    private MutableLiveData<UpdateCandidateCentreAPIResponse> updateCandidateCentreAPIResponseMutableLiveData;


    public UpdateCandidateCentreRepository() {

        updateCandidateCentreAPIResponseMutableLiveData = new MutableLiveData<>();
    }

    // call update centre api
    public void callUpdateCandidateCentreApi(@NonNull String token, @NonNull String rollNumber){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<UpdateCandidateCentreAPIResponse> call = apiDataService.UpdateCandidateCentre(token, new UpdateCandidateCentreBody(rollNumber));
        call.enqueue(new Callback<UpdateCandidateCentreAPIResponse>() {
            @Override
            public void onResponse(Call<UpdateCandidateCentreAPIResponse> call, Response<UpdateCandidateCentreAPIResponse> response) {
                if (response.body() != null) {
                    updateCandidateCentreAPIResponseMutableLiveData.postValue(response.body());
                }else{
                    updateCandidateCentreAPIResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdateCandidateCentreAPIResponse> call, Throwable t) {
                updateCandidateCentreAPIResponseMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<UpdateCandidateCentreAPIResponse> updateCandidateCentreResponseLiveData() {
        return updateCandidateCentreAPIResponseMutableLiveData;
    }

}
