package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.daos.DayDao;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;

import java.util.List;

public class DayDaoRepository {
    private DayDao dayDao;
    private LiveData<List<DayEntity>> getAllDays;

    public DayDaoRepository(Application application) {
        StatusTruckRoomDatabase db = StatusTruckRoomDatabase.getINSTANCE(application);
        dayDao = db.dayDao();
        getAllDays = dayDao.getAllDays();
    }

    public LiveData<List<DayEntity>> getGetAllDays() {
        return getAllDays;
    }

    public void insertDay(DayEntity dayEntity) {
        new insertDayAsync(dayDao).execute(dayEntity);
    }

    public void deleteDay(DayEntity entity) {new deleterDayAsync(dayDao).execute(entity);}

    private static class insertDayAsync extends AsyncTask<DayEntity, Void, Void> {
        private DayDao dao;

        insertDayAsync(DayDao dayDao) {this.dao = dayDao;}

        @Override
        protected Void doInBackground(DayEntity... dayEntities) {
            dao.insertDay(dayEntities[0]);
            return null;
        }
    }

    private static class deleterDayAsync extends AsyncTask<DayEntity, Void, Void> {
        private DayDao dao;

        deleterDayAsync(DayDao dayDao) {this.dao = dayDao;}

        @Override
        protected Void doInBackground(DayEntity... dayEntities) {
            dao.deleteDay(dayEntities[0]);
            return null;
        }
    }
}
