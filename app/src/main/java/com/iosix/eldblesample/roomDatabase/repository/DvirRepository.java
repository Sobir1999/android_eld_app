package com.iosix.eldblesample.roomDatabase.repository;

import static com.iosix.eldblesample.enums.Day.stringToDay;

import android.app.Application;
import android.util.Log;

import com.iosix.eldblesample.models.Dvir;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class DvirRepository {

    private StatusTruckRoomDatabase db;

    public DvirRepository(Application application){
        db = StatusTruckRoomDatabase.getINSTANCE(application);
    }

    public Single<List<Dvir>> getAllDvirs() {
        return db.dvirDao().getDvirs();
    }

    public Single<List<Dvir>> getCurrDateDvirs(){
        return db.dvirDao().getCurrDateDvirs();
    }

    public void insertDvir(Dvir dvirEntity) {
         db.dvirDao().insertDvir(dvirEntity);
    }

    public Completable deleteDvir(Dvir entity) {
        return db.dvirDao().deleteDvir(entity);
    }

    public Completable updateDvir(Dvir entity) {
        return db.dvirDao().update(entity);
    }

}
