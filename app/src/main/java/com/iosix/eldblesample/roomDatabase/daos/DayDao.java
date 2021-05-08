package com.iosix.eldblesample.roomDatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.roomDatabase.entities.DayEntity;

import java.util.List;

@Dao
public interface DayDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertDay(DayEntity entity);

    @Query("Select * From day_table")
    LiveData<List<DayEntity>> getAllDays();

    @Query("Delete From day_table")
    void deleteAllDay();
}
