package com.iosix.eldblesample.roomDatabase.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.roomDatabase.entities.DayEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertDay(DayEntity entity);

    @Query("Select * From day_table ORDER BY CAST(day as DATETIME) ASC")
    Single<List<DayEntity>> getAllDays();

    @Delete
    Completable deleteDay(DayEntity entity);

    @Query("Delete From day_table")
    Completable deleteAllDay();

}
