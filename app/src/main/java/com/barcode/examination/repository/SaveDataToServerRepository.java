package com.barcode.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barcode.examination.connection.ApiDataService;
import com.barcode.examination.connection.RetrofitInstance;
import com.barcode.examination.model.DataToServer;
import com.barcode.examination.model.SaveDataToServer;
import com.barcode.examination.model.SaveDataToServerResponse.SaveDataToServerAPIResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveDataToServerRepository {

    private MutableLiveData<SaveDataToServerAPIResponse> saveDataToServerResponseMutableLiveData;


    public SaveDataToServerRepository() {

        saveDataToServerResponseMutableLiveData = new MutableLiveData<>();
    }

    // call sync data api
    public void callSaveDataToServerAPI(@NonNull String token, @NonNull List<DataToServer> list){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<SaveDataToServerAPIResponse> call = apiDataService.saveDataToServer(token,new SaveDataToServer(list));
        call.enqueue(new Callback<SaveDataToServerAPIResponse>() {
            @Override
            public void onResponse(Call<SaveDataToServerAPIResponse> call, Response<SaveDataToServerAPIResponse> response) {
                if (response.body() != null) {
                    saveDataToServerResponseMutableLiveData.postValue(response.body());
                }else{
                    saveDataToServerResponseMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<SaveDataToServerAPIResponse> call, Throwable t) {
                saveDataToServerResponseMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<SaveDataToServerAPIResponse> saveDataToServerResponseLiveData() {
        return saveDataToServerResponseMutableLiveData;
    }

}
