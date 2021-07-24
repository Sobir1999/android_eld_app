package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.daos.DayDao;
import com.iosix.eldblesample.roomDatabase.daos.SignatureDao;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SignatureRepository {

    private SignatureDao signatureDao;
    private LiveData<List<SignatureEntity>> getAllSignatures;

    public SignatureRepository(Application application) {
        StatusTruckRoomDatabase db = StatusTruckRoomDatabase.getINSTANCE(application);
        signatureDao = db.getSignatureDao();
        getAllSignatures = signatureDao.getSignature();
    }

    public LiveData<List<SignatureEntity>> getGetAllSignatures() {
        return getAllSignatures;

    }

    public Long insertSignature(SignatureEntity signatureEntity) throws ExecutionException, InterruptedException {
        return new SignatureRepository.insertSignatureAsync(signatureDao).execute(signatureEntity).get();
    }

    private static class insertSignatureAsync extends AsyncTask<SignatureEntity, Void, Long> {
        private SignatureDao dao;

        insertSignatureAsync(SignatureDao signatureDao) {
            this.dao = signatureDao;
        }

        @Override
        protected Long doInBackground(SignatureEntity... signatureEntities) {

            return dao.insertSignature(signatureEntities[0]);
        }
    }
}

