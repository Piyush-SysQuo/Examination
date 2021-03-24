package com.inspection.examination.database.db;

import android.content.Context;

import androidx.room.Room;

import com.inspection.examination.util.Constants;

public class CandidatesDBClient {
    private static CandidatesDBClient mInstance;

    //our app database object
    private CandidatesDatabase appDatabase;


    private CandidatesDBClient(Context mCtx) {

        //creating the app database with Room database builder
        //KEY_DATABASE_NAME is the name of the database
        appDatabase = Room.databaseBuilder(mCtx.getApplicationContext(),CandidatesDatabase.class, Constants.KEY_DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized CandidatesDBClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new CandidatesDBClient(mCtx);
        }
        return mInstance;
    }

    public CandidatesDatabase getAppDatabase() {
        return appDatabase;
    }
}
