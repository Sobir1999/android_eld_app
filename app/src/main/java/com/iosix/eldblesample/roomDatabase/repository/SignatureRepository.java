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

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class SignatureRepository {

    private final StatusTruckRoomDatabase db;

    public SignatureRepository(Application application) {
        db = StatusTruckRoomDatabase.getINSTANCE(application);
    }

    public Single<List<SignatureEntity>> getGetAllSignatures() {
        return db.getSignatureDao().getSignature();
    }

    public void insertSignature(SignatureEntity signatureEntity){
        db.getSignatureDao().insertSignature(signatureEntity);
    }
}

