package com.barcode.examination.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.barcode.examination.connection.ApiDataService;
import com.barcode.examination.connection.RetrofitInstance;
import com.barcode.examination.model.DataToServer;
import com.barcode.examination.model.SaveDataToServerResp;
import com.barcode.examination.model.SaveDataToServerResponse.SaveDataToServerAPIResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveDataToServerRepo {

    private MutableLiveData<SaveDataToServerResp> liveData;


    public SaveDataToServerRepo() {

        liveData = new MutableLiveData<>();
    }

    // save local data to server
    public void callApi(@NonNull List<DataToServer> list ){
       ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
       Call<SaveDataToServerAPIResponse> resp = apiDataService.saveDataToServer("",null);
       resp.enqueue(new Callback<SaveDataToServerAPIResponse>() {
           @Override
           public void onResponse(Call<SaveDataToServerAPIResponse> call, Response<SaveDataToServerAPIResponse> response) {
               if (response.body() != null) {
                   liveData.postValue(null);
               }else{
                   liveData.postValue(null);
               }
           }

           @Override
           public void onFailure(Call<SaveDataToServerAPIResponse> call, Throwable t) {
               liveData.postValue(null);
           }
       });
    }

    public LiveData<SaveDataToServerResp> dataToServerLiveData() {
        return liveData;
    }


}
