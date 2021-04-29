package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.iosix.eldblesample.roomDatabase.daos.StatusTruckDao;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.TruckStatusEntity;

import java.util.List;

public class StatusTableRepositories {

    private StatusTruckDao mStatusDao;
    private LiveData<List<TruckStatusEntity>> mAllStatus;

    public StatusTableRepositories(Application application) {
        StatusTruckRoomDatabase db = StatusTruckRoomDatabase.getINSTANCE(application);
        mStatusDao = db.statusTruckDao();
        mAllStatus = mStatusDao.getAllStatus();
    }

    public LiveData<List<TruckStatusEntity>> getmAllStatus() {
        return mAllStatus;
    }

    public void insertStatus(TruckStatusEntity entity) {
        new insertStatusAsync(mStatusDao).execute(entity);
    }

    private static class insertStatusAsync extends AsyncTask<TruckStatusEntity, Void, Void> {
        private StatusTruckDao dao;

        insertStatusAsync(StatusTruckDao statusTruckDao) {
            dao = statusTruckDao;
        }

        @Override
        protected Void doInBackground(TruckStatusEntity... truckStatusEntities) {
            dao.insertStatus(truckStatusEntities[0]);
            return null;
        }
    }
}
