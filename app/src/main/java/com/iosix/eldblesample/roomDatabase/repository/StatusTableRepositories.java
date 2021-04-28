package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.daos.StatusTruckDao;
import com.iosix.eldblesample.roomDatabase.database.StatusRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.TruckStatusEntity;

import java.util.List;

public class StatusTableRepository {
    private StatusTruckDao mStatusDao;
    private LiveData<List<TruckStatusEntity>> mAllStatus;

    public StatusTableRepository(Application application) {
        StatusRoomDatabase db = new StatusRoomDatabase.getINSTANCE(application);
        mStatusDao = db.statusTruckDao();
        mAllStatus = mStatusDao.getAllStatus();
    }

    public LiveData<List<TruckStatusEntity>> getmAllStatus() {
        return mAllStatus;
    }

    public void insertStatus(TruckStatusEntity entity) {

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
