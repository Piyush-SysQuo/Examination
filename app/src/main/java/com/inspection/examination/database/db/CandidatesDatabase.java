package com.inspection.examination.database.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.inspection.examination.model.SyncDataResponse.CandidatesList;
import com.inspection.examination.util.Constants;

@Database(entities = {CandidatesList.class}, version = 1)
public abstract class CandidatesDatabase extends RoomDatabase {

    private static CandidatesDatabase instance;

    public abstract CandidatesDAO candidatesDAO();

    public static synchronized CandidatesDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),CandidatesDatabase.class, Constants.KEY_DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

//    private static RoomDatabase.Callback roomCallback = RoomDatabase.Callback(){
//
//    };
}
