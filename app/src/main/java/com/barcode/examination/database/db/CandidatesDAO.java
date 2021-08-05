package com.barcode.examination.database.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.barcode.examination.model.SyncDataResponse.CandidatesList;
import com.barcode.examination.util.Constants;

import java.util.List;

@Dao
public interface CandidatesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCandidatesData(List<CandidatesList> data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIndividualCandidateData(CandidatesList data);

//    @Update
//    int updateOccurrance(String rollNumber, int count);

    /*get all data*/
    @Query("SELECT * FROM "+ Constants.KEY_TABLE_NAME)
    LiveData<List<CandidatesList>> getAllData();

    /*get all data from db*/
    @Query("SELECT * FROM "+ Constants.KEY_TABLE_NAME)
    CandidatesList[] getAllCandidatesData();

    /*get single student data*/
    @Query("SELECT * FROM "+ Constants.KEY_TABLE_NAME +" WHERE "+ Constants.KEY_ROLL_NUMBER +" LIKE:rollNo")
    LiveData<CandidatesList> getStudentData(String rollNo);

    // update occurrence field query
    @Query("UPDATE "+ Constants.KEY_TABLE_NAME +" SET "+ Constants.KEY_OCCURRANCE +" = :count WHERE "+ Constants.KEY_ROLL_NUMBER +" = :rollNumber")
    void updateOccurranceForStudent(String rollNumber, int count);

    /*get occurrence from db*/
    @Query("SELECT "+ Constants.KEY_OCCURRANCE +" FROM "+ Constants.KEY_TABLE_NAME +" WHERE "+ Constants.KEY_ROLL_NUMBER +" LIKE:rollNo")
    int getOccurrenceForStudent(String rollNo);

    /*get single student data*/
    @Query("SELECT * FROM "+ Constants.KEY_TABLE_NAME +" WHERE "+ Constants.KEY_ROLL_NUMBER +" LIKE:rollNo")
    CandidatesList getSingleStudentData(String rollNo);
}
