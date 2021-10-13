package com.iosix.eldblesample.roomDatabase.daos;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;

import java.util.List;

@Dao
public interface DVIRDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertDvir(DvirEntity dvirEntity);


    @Query("SELECT * FROM dvir_table")
    public LiveData<List<DvirEntity>> getDvirs();

    @Delete
    void deleteDvir(DvirEntity dvirEntity);


}
