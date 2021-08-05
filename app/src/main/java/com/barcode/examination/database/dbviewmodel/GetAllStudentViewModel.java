package com.barcode.examination.database.dbviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.barcode.examination.database.db.DataBaseClient;
import com.barcode.examination.model.ListStudent;

import java.util.List;

public class GetAllStudentViewModel extends AndroidViewModel {


    private static DataBaseClient mInstance;

    public GetAllStudentViewModel(@NonNull Application application) {
        super(application);
        mInstance = DataBaseClient.getInstance(application.getApplicationContext());
    }

    public LiveData<List<ListStudent>> getAllStudent(){
        return mInstance.getAppDatabase().examinationDao().getAllData();
    }
}
