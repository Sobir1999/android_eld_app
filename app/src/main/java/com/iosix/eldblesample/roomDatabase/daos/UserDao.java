package com.iosix.eldblesample.roomDatabase.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.VehicleList;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("Select * From user_table GROUP BY driver_license_number")
    Single<List<User>> getDrivers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertLocalData(LiveDataRecord liveDataRecord);

    @Query("Select * From local_data_table")
    Single<List<LiveDataRecord>> getLocalDatas();

    @Query("Delete From local_data_table")
    void deletLocalDatas();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVehicle(VehicleList vehicle);

    @Query("Select * From vehicles GROUP BY license_plate_num")
    Single<List<VehicleList>> getAllVehicles();

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insertTrailer(TrailersEntity trailers);

    @Query("Select * From trailers")
    Single<List<TrailersEntity>> getAllTrailers();
}
