package com.iosix.eldblesample.roomDatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.roomDatabase.entities.TruckStatusEntity;

import java.util.List;

@Dao
public interface StatusTruckDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public void insertStatus(TruckStatusEntity truckStatusEntity);

    @Query("Select * from status_table")
    LiveData<List<TruckStatusEntity>> getAllStatus();

    @Query("Delete from status_table")
    void deleteAllTruckStatus();
}
