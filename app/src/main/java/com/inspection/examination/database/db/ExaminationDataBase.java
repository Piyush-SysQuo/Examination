package com.inspection.examination.database.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.inspection.examination.database.dbmodel.ListOccData;
import com.inspection.examination.database.dbmodel.StudentDetailsEntity;
import com.inspection.examination.model.ListStudent;


@Database(entities = {ListStudent.class}, version = 1)
public abstract class ExaminationDataBase extends RoomDatabase {

    public abstract ExaminationDao examinationDao();

}
