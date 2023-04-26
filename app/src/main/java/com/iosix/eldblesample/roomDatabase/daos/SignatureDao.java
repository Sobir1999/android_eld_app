package com.iosix.eldblesample.roomDatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.roomDatabase.entities.MechanicSignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface SignatureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSignature(SignatureEntity signature);

    @Query("SELECT * FROM signature_table GROUP BY signature")
    Single<List<SignatureEntity>> getSignature();

}
