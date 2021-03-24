package com.inspection.examination.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.inspection.examination.connection.ApiDataService;
import com.inspection.examination.connection.RetrofitInstance;
import com.inspection.examination.model.StudentAvailabilityBody;
import com.inspection.examination.model.StudentAvailabilityList;
import com.inspection.examination.model.StudentAvailabilityResp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresentStudentRepo {

    private MutableLiveData<StudentAvailabilityResp> mutableLiveData;


    public PresentStudentRepo() {

        mutableLiveData = new MutableLiveData<>();
    }

    // call get absent student data
    public void getPresentData(int pages, int limit){
        ApiDataService apiDataService = RetrofitInstance.getInstance().create(ApiDataService.class);
        Call<StudentAvailabilityResp> call = apiDataService.present(new StudentAvailabilityBody(pages,limit));
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
