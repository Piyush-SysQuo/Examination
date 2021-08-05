package com.barcode.examination.database.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.barcode.examination.model.ListStudent;


@Database(entities = {ListStudent.class}, version = 1)
public abstract class ExaminationDataBase extends RoomDatabase {

    public abstract ExaminationDao examinationDao();

}
