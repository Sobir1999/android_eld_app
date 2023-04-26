package com.iosix.eldblesample.roomDatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface GeneralDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGeneral(GeneralEntity generalEntity);


    @Query("SELECT * FROM general")
    Single<List<GeneralEntity>> getGenerals();

    @Query("SELECT * FROM general WHERE day LIKE :day")
    Single<List<GeneralEntity>> getCurDayGenerals(String day);

    @Delete
    void deleteGeneral(GeneralEntity generalEntity);
}
