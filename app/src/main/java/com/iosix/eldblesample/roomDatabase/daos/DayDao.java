package com.iosix.eldblesample.roomDatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;

import java.util.List;

@Dao
public interface DayDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertDay(DayEntity entity);

    @Query("Select * From day_table")
    LiveData<List<DayEntity>> getAllDays();

    @Delete
    void deleteDay(DayEntity entity);

    @Query("Delete From day_table")
    void deleteAllDay();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public void insertStatus(LogEntity logEntity);

    @Query("Select * from log_table")
    LiveData<List<LogEntity>> getAllStatus();

    @Query("DELETE FROM log_table WHERE time = :day")
    void deleteAllTruckStatus(String day);

    @Insert
    void insertVehicle(VehiclesEntity vehiclesEntity);

    @Query("Select * From vehicles")
    LiveData<List<VehiclesEntity>> getAllVehicles();

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    long insertTrailer(TrailersEntity trailers);

    @Query("Select * From trailers")
    LiveData<List<TrailersEntity>> getAllTrailers();
}
