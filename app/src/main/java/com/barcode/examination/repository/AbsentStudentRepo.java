package com.barcode.examination.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.barcode.examination.connection.ApiDataService;
import com.barcode.examination.connection.RetrofitInstance;
import com.barcode.examination.model.StudentAvailabilityBody;
import com.barcode.examination.model.StudentAvailabilityResp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsentStudentRepo {

    private MutableLiveData<StudentAvailabilityResp> mutableLiveData;


    public AbsentStudentRepo() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call get absent student data
    public void getAbsentData(int pages, int limit){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<StudentAvailabilityResp> call = apiDataService.absent(new StudentAvailabilityBody(pages,limit));
        call.enqueue(new Callback<StudentAvailabilityResp>() {
            @Override
            public void onResponse(Call<StudentAvailabilityResp> call, Response<StudentAvailabilityResp> response) {
                if (response.body() != null) {
                    mutableLiveData.postValue(response.body());
                }else{
                    mutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<StudentAvailabilityResp> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<StudentAvailabilityResp> data() {
        return mutableLiveData;
    }


}
