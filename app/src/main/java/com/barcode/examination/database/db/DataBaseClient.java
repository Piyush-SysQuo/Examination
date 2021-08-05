package com.barcode.examination.database.db;

import android.content.Context;
import androidx.room.Room;

public class DataBaseClient {

    private static DataBaseClient mInstance;

    //our app database object
    private ExaminationDataBase appDatabase;


    private DataBaseClient(Context mCtx) {

        //creating the app database with Room database builder
        //StudentDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, ExaminationDataBase.class, "StudentDos") .build();
    }

    public static synchronized DataBaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DataBaseClient(mCtx);
        }
        return mInstance;
    }

    public ExaminationDataBase getAppDatabase() {
        return appDatabase;
    }

}
