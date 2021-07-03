package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.daos.StatusTruckDao;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;

import java.util.List;

public class StatusTableRepositories {

    private StatusTruckDao mStatusDao;
    private LiveData<List<LogEntity>> mAllStatus;

    public StatusTableRepositories(Application application) {
        StatusTruckRoomDatabase db = StatusTruckRoomDatabase.getINSTANCE(application);
        mStatusDao = db.statusTruckDao();
        mAllStatus = mStatusDao.getAllStatus();
    }

    public LiveData<List<LogEntity>> getmAllStatus() {
        return mAllStatus;
    }

    public void insertStatus(LogEntity entity) {
        new insertStatusAsync(mStatusDao).execute(entity);
    }

    private static class insertStatusAsync extends AsyncTask<LogEntity, Void, Void> {
        private StatusTruckDao dao;

        insertStatusAsync(StatusTruckDao statusTruckDao) {
            dao = statusTruckDao;
        }

        @Override
        protected Void doInBackground(LogEntity... truckStatusEntities) {
            dao.insertStatus(truckStatusEntities[0]);
            return null;
        }
    }
}
