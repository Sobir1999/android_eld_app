package com.iosix.eldblesample.roomDatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;

import java.util.List;

@Dao
public interface GeneralDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertGeneral(GeneralEntity generalEntity);


    @Query("SELECT * FROM dvir_table")
    public LiveData<List<GeneralEntity>> getGenerals();

    @Delete
    void deleteGeneral(GeneralEntity generalEntity);
}
