package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.daos.DVIRDao;
import com.iosix.eldblesample.roomDatabase.daos.DayDao;
import com.iosix.eldblesample.roomDatabase.daos.SignatureDao;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DvirRepository {

    private DVIRDao dvirDao;
    private LiveData<List<DvirEntity>> getDvirs;

    public DvirRepository(Application application){
        StatusTruckRoomDatabase db = StatusTruckRoomDatabase.getINSTANCE(application);
        dvirDao = db.dvirDao();
        getDvirs = dvirDao.getDvirs();
    }

    public LiveData<List<DvirEntity>> getAllDvirs(){
        return getDvirs;
    }

    public Long insertDvir(DvirEntity dvirEntity) throws ExecutionException, InterruptedException {
        return new DvirRepository.insertDvirAsync(dvirDao).execute(dvirEntity).get();
    }

    private static class insertDvirAsync extends AsyncTask<DvirEntity, Void, Long> {
        private DVIRDao dao;

        insertDvirAsync(DVIRDao dvirDao) {
            this.dao = dvirDao;
        }


        @Override
        protected Long doInBackground(DvirEntity... dvirEntities) {
            return dao.insertDvir(dvirEntities[0]);
        }
    }

    public void deleteDvir(DvirEntity entity) {
        new DvirRepository.deleteDvirAsync(dvirDao).execute(entity);
    }

    private static class deleteDvirAsync extends AsyncTask<DvirEntity, Void, Long> {
        private DVIRDao dao;

        deleteDvirAsync(DVIRDao dvirDao) {
            this.dao = dvirDao;
        }

        @Override
        protected Long doInBackground(DvirEntity... dvirEntities) {
            dao.deleteDvir(dvirEntities[0]);
            return null;
        }
    }

}
