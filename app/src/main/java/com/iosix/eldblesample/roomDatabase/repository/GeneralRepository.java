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

public class GeneralRepository {

    private final GeneralDao generalDao;
    private final LiveData<List<GeneralEntity>> getGeneral;


    public GeneralRepository(Application application) {
        StatusTruckRoomDatabase db = StatusTruckRoomDatabase.getINSTANCE(application);
        generalDao = db.generalDao();
        getGeneral = generalDao.getGenerals();
    }

    public LiveData<List<GeneralEntity>> getGeneral() {
        return getGeneral;
    }

    public Long insertGeneral(GeneralEntity generalEntity) throws ExecutionException, InterruptedException {
        return new GeneralRepository.insertGeneralAsync(generalDao).execute(generalEntity).get();
    }

    public void deleteGeneral(GeneralEntity generalEntity) {
        new GeneralRepository.deleteGeneralAsync(generalDao).execute();
    }

    private static class insertGeneralAsync extends AsyncTask<GeneralEntity, Void, Long> {
        private GeneralDao generalDao;

        insertGeneralAsync(GeneralDao generalDao) {
            this.generalDao = generalDao;
        }

        @Override
        protected Long doInBackground(GeneralEntity... generalEntities) {
            return generalDao.insertGeneral(generalEntities[0]);
        }
    }

    private static class deleteGeneralAsync extends AsyncTask<GeneralEntity, Void, Void> {
        private GeneralDao generalDao;

        deleteGeneralAsync(GeneralDao generalDao) {
            this.generalDao = generalDao;
        }

        @Override
        protected Void doInBackground(GeneralEntity... generalEntities) {
            generalDao.deleteGeneral(generalEntities[0]);
            return null;
        }
    }
}
