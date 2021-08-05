package com.barcode.examination.database.dbviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.barcode.examination.database.db.DataBaseClient;
import com.barcode.examination.model.ListStudent;


public class GetSingleStudentData extends AndroidViewModel {

    private static DataBaseClient mInstance;

    public GetSingleStudentData(@NonNull Application application) {
        super(application);
        mInstance = DataBaseClient.getInstance(application.getApplicationContext());
    }

    public LiveData<ListStudent> getSingleStudent(@NonNull String rollNo){
        return mInstance.getAppDatabase().examinationDao().getStudentData(rollNo);
    }
}
