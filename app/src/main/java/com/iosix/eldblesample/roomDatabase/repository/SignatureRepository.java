package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.daos.DayDao;
import com.iosix.eldblesample.roomDatabase.daos.SignatureDao;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.MechanicSignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SignatureRepository {

    private SignatureDao signatureDao;
    private LiveData<List<SignatureEntity>> getAllSignatures;
    private LiveData<List<MechanicSignatureEntity>> getAllMechanicSignatures;

    public SignatureRepository(Application application) {
        StatusTruckRoomDatabase db = StatusTruckRoomDatabase.getINSTANCE(application);
        signatureDao = db.getSignatureDao();
        getAllSignatures = signatureDao.getSignature();
        getAllMechanicSignatures = signatureDao.getMechanicSignature();
    }

    public LiveData<List<SignatureEntity>> getGetAllSignatures() {
        return getAllSignatures;
    }

    public LiveData<List<MechanicSignatureEntity>> getGetAllMechanicSignatures() {
        return getAllMechanicSignatures;
    }

    public Long insertSignature(SignatureEntity signatureEntity) throws ExecutionException, InterruptedException {
        return new SignatureRepository.insertSignatureAsync(signatureDao).execute(signatureEntity).get();
    }

    public Long insertMechanicSignature(MechanicSignatureEntity mechanicSignatureEntity) throws ExecutionException, InterruptedException {
        return new SignatureRepository.insertMechanicSignatureAsync(signatureDao).execute(mechanicSignatureEntity).get();
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

    private static class insertMechanicSignatureAsync extends AsyncTask<MechanicSignatureEntity,Void,Long>{
        private SignatureDao dao;

        insertMechanicSignatureAsync(SignatureDao signatureDao){this.dao = signatureDao;}

        @Override
        protected Long doInBackground(MechanicSignatureEntity... mechanicSignatureEntities) {
            return dao.insertMechanicSignature(mechanicSignatureEntities[0]);
        }
    }
}

