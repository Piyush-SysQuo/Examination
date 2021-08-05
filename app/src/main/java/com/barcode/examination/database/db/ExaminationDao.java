package com.barcode.examination.database.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.barcode.examination.model.ListStudent;
import java.util.List;

@Dao
public interface ExaminationDao {

    /*insert all data into database*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllDataStudentData(List<ListStudent> data);

     /*get all data*/
    @Query("SELECT * FROM ListStudent")
    LiveData<List<ListStudent>> getAllData();

     /*get single student data*/
    @Query("SELECT * FROM liststudent WHERE rollNo LIKE:rollNo")
    LiveData<ListStudent> getStudentData(String rollNo);

    /*get single student data*/
    @Query("SELECT * FROM liststudent WHERE rollNo LIKE:rollNo")
    ListStudent getStudentSingleData(String rollNo);

    // update occurrence field query
    @Query("UPDATE liststudent SET occurrence = :times , count = :count WHERE rollno = :rollNumber")
    int updateOcc(String rollNumber, long times, long count);

    /*get occurrence from db*/
    @Query("SELECT * FROM liststudent")
    ListStudent[] getData();

    /*get occurrence from db*/
    @Query("UPDATE liststudent SET count = :times")
    int updateCount(long times);

    // update occurrence field query
    @Query("UPDATE liststudent SET occurrence = :times  WHERE rollno = :rollNumber")
    int updateOccServer(String rollNumber, long times);

    @Query("SELECT occurrence FROM liststudent WHERE rollno LIKE:rollNo")
    long getOccurrence(String rollNo);
}
