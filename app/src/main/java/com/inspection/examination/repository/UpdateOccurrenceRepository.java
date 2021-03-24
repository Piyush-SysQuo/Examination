package com.inspection.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inspection.examination.connection.ApiDataService;
import com.inspection.examination.connection.RetrofitInstance;
import com.inspection.examination.model.LoginBody;
import com.inspection.examination.model.LoginResponse.LoginAPIResponse;
import com.inspection.examination.model.UpdateOccurrenceBody;
import com.inspection.examination.model.UpdateOccurrenceResponse.UpdateOccurrenceAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateOccurrenceRepository {

    private MutableLiveData<UpdateOccurrenceAPIResponse> updateOccurrenceResponseMutableLiveData;


    public UpdateOccurrenceRepository() {

        updateOccurrenceResponseMutableLiveData = new MutableLiveData<>();
    }

    // call update occurrence api
    public void callUpdateOccurrenceApi(@NonNull String token, @NonNull String rollNumber){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<UpdateOccurrenceAPIResponse> call = apiDataService.UpdateCandidateOccurrence(token, new UpdateOccurrenceBody(rollNumber));
        call.enqueue(new Callback<UpdateOccurrenceAPIResponse>() {
            @Override
            public void onResponse(Call<UpdateOccurrenceAPIResponse> call, Response<UpdateOccurrenceAPIResponse> response) {
                if (response.body() != null) {
                    updateOccurrenceResponseMutableLiveData.postValue(response.body());
                }else{
                    updateOccurrenceResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<UpdateOccurrenceAPIResponse> call, Throwable t) {
                updateOccurrenceResponseMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<UpdateOccurrenceAPIResponse> updateOccurrenceResponseLiveData() {
        return updateOccurrenceResponseMutableLiveData;
    }

}
