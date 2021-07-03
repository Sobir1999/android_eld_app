package com.iosix.eldblesample.roomDatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.roomDatabase.entities.LogEntity;

import java.util.List;

@Dao
public interface StatusTruckDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public void insertStatus(LogEntity logEntity);

    @Query("Select * from log_table")
    LiveData<List<LogEntity>> getAllStatus();

    @Query("DELETE FROM log_table WHERE time = :day")
    void deleteAllTruckStatus(String day);
}
