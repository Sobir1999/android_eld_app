package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;

import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class DayDaoRepository {
    private final StatusTruckRoomDatabase db;


    public DayDaoRepository(Application application) {
        db = StatusTruckRoomDatabase.getINSTANCE(application);
    }

    public Single<List<DayEntity>> getGetAllDays() {
        return db.dayDao().getAllDays();
    }


    public Completable insertDay(DayEntity dayEntity) {
        return db.dayDao().insertDay(dayEntity);
    }

    public Completable deleteDay(DayEntity entity) {
        return db.dayDao().deleteDay(entity);
    }

    public Completable deleteAllDays() {
        return db.dayDao().deleteAllDay();
    }

}
