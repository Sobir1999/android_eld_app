package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;

import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;


public class StatusTableRepositories {

    private StatusTruckRoomDatabase db;

    public StatusTableRepositories(Application application) {
        db = StatusTruckRoomDatabase.getINSTANCE(application);
    }

    public Single<List<Status>> getmAllStatus(String day) {
        return db.statusTruckDao().getAllStatus(day);
    }

    public Single<List<Status>> getmActionStatus(List<String> statuses) {
        return db.statusTruckDao().getActionStatus(statuses);
    }

    public Single<List<Status>> getCurrDateStatuses(List<String> statuses,String day) {
        return db.statusTruckDao().getCurrDAteList(statuses,day);
    }

    public Completable insertStatus(Status entity) {
        return db.statusTruckDao().insertStatus(entity);
    }
}
