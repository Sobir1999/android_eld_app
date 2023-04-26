package com.iosix.eldblesample.roomDatabase.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.models.Status;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface StatusTruckDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertStatus(Status logEntity);

    @Query("Select * from log_table WHERE time LIKE '%' || :day || '%' GROUP BY time ORDER BY CAST(time as DATETIME)")
    Single<List<Status>> getAllStatus(String day);

    @Query("Select * from log_table WHERE to_status IN (:statuses) GROUP BY time ORDER BY CAST(time as DATETIME)")
    Single<List<Status>> getActionStatus(List<String> statuses);

    @Query("Select * from log_table WHERE to_status IN (:statuses) AND time LIKE '%' || :day || '%' GROUP BY time ORDER BY CAST(time as DATETIME)")
    Single<List<Status>> getCurrDAteList(List<String> statuses,String day);

    @Query("DELETE FROM log_table WHERE time = :day")
    void deleteAllTruckStatus(String day);
}
