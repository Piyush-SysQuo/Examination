package com.barcode.examination.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barcode.examination.connection.ApiDataService;
import com.barcode.examination.connection.RetrofitInstance;
import com.barcode.examination.model.AllStudentDataResp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllStdDataRepo {

    private MutableLiveData<AllStudentDataResp> allStudentDataRespMutableLiveData;


    public GetAllStdDataRepo() {

        allStudentDataRespMutableLiveData = new MutableLiveData<>();
    }

    // call get all student api
    public void calGetAllStudentListApi(){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<AllStudentDataResp> resp = apiDataService.getAllStudentData();
        resp.enqueue(new Callback<AllStudentDataResp>() {
            @Override
            public void onResponse(Call<AllStudentDataResp> call, Response<AllStudentDataResp> response) {
                if (response.body() != null) {
                    allStudentDataRespMutableLiveData.postValue(response.body());
                }else {
                    allStudentDataRespMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<AllStudentDataResp> call, Throwable t) {
                allStudentDataRespMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<AllStudentDataResp> allStudentDataRespLiveData() {
        return allStudentDataRespMutableLiveData;
    }
}
