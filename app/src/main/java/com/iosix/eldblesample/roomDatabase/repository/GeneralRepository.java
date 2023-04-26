package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.roomDatabase.daos.GeneralDao;
import com.iosix.eldblesample.roomDatabase.daos.UserDao;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class GeneralRepository {

    private final StatusTruckRoomDatabase db;


    public GeneralRepository(Application application) {
        db = StatusTruckRoomDatabase.getINSTANCE(application);
    }

    public Single<List<GeneralEntity>> getGeneral() {
        return db.generalDao().getGenerals();
    }

    public Single<List<GeneralEntity>> getCurrDayGeneral(String day) {
        return db.generalDao().getCurDayGenerals(day);
    }

    public void insertGeneral(GeneralEntity generalEntity){
        db.generalDao().insertGeneral(generalEntity);
    }

    public void deleteGeneral(GeneralEntity generalEntity) {
        db.generalDao().deleteGeneral(generalEntity);
    }
}
