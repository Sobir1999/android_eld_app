package com.iosix.eldblesample.roomDatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("Select * From user_table")
    LiveData<List<User>> getDrivers();

    @Query("Select * From user_table")
    LiveData<User> getUser();

    @Query("Delete From user_table")
    void deleteUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocalData(LiveDataRecord liveDataRecord);

    @Query("Select * From local_data_table")
    LiveData<List<LiveDataRecord>> getLocalDatas();

    @Query("Delete From local_data_table")
    void deletLocalDatas();

    @Insert
    void insertVehicle(VehiclesEntity vehiclesEntity);

    @Query("Select * From vehicles")
    LiveData<List<VehiclesEntity>> getAllVehicles();

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    long insertTrailer(TrailersEntity trailers);

    @Query("Select * From trailers")
    LiveData<List<TrailersEntity>> getAllTrailers();
}
