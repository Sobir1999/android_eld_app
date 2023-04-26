package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;

import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.VehicleList;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class UserRepository {
    private final StatusTruckRoomDatabase db;
    public UserRepository(Application application) {
        db = StatusTruckRoomDatabase.getINSTANCE(application);
    }

    public void insertUser(User user) {
        db.userDao().insertUser(user);
    }

    public Single<List<User>> getDrivers() {
        return db.userDao().getDrivers();
    }

    public Single<List<LiveDataRecord>> getLocalDatas(){ return db.userDao().getLocalDatas();}

    public Completable insertLocalData(LiveDataRecord liveDataRecord){return db.userDao().insertLocalData(liveDataRecord);}

    public void deleteLocalData(){db.userDao().deletLocalDatas();}

    public Single<List<TrailersEntity>> getGetAllTrailers() {
        return db.userDao().getAllTrailers();
    }

    public Single<List<VehicleList>> getGetAllVehicles() {
        return db.userDao().getAllVehicles();
    }

    public void insertVehicle(VehicleList entity) {
        db.userDao().insertVehicle(entity);
    }
}
