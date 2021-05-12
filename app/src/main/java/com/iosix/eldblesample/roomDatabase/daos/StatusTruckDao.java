package com.iosix.eldblesample.roomDatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.roomDatabase.entities.TruckStatusEntity;

import java.util.List;

import kotlin.jvm.JvmOverloads;

@Dao
public interface StatusTruckDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public void insertStatus(TruckStatusEntity truckStatusEntity);

    @Query("Select * from status_table")
    LiveData<List<TruckStatusEntity>> getAllStatus();

//    @JvmOverloads
//    @Query("Select * From status_table where time like '% : day1 %' order by seconds")
//    LiveData<List<TruckStatusEntity>> getTruckEntityByDay(String day1);

    @Query("Delete from status_table")
    void deleteAllTruckStatus();
}
