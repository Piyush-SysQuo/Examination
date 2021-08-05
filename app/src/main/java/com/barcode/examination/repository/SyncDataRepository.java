package com.barcode.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barcode.examination.connection.ApiDataService;
import com.barcode.examination.connection.RetrofitInstance;
import com.barcode.examination.model.SyncDataResponse.SyncDataAPIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncDataRepository {

    private MutableLiveData<SyncDataAPIResponse> syncDataResponseMutableLiveData;


    public SyncDataRepository() {

        syncDataResponseMutableLiveData = new MutableLiveData<>();
    }

    // call sync data api
    public void callSyncDataAPI(@NonNull String token){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<SyncDataAPIResponse> call = apiDataService.SyncCandidatesList(token);
        call.enqueue(new Callback<SyncDataAPIResponse>() {
            @Override
            public void onResponse(Call<SyncDataAPIResponse> call, Response<SyncDataAPIResponse> response) {
                if (response.body() != null) {
                    syncDataResponseMutableLiveData.postValue(response.body());
                }else{
                    syncDataResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<SyncDataAPIResponse> call, Throwable t) {
                syncDataResponseMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<SyncDataAPIResponse> syncDataResponseLiveData() {
        return syncDataResponseMutableLiveData;
    }

}
