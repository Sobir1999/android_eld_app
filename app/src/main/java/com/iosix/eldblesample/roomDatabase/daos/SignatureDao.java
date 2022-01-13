package com.iosix.eldblesample.roomDatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.roomDatabase.entities.MechanicSignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;

import java.util.List;

@Dao
public interface SignatureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertSignature(SignatureEntity signature);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertMechanicSignature(MechanicSignatureEntity mechanicSignature);

    @Query("SELECT * FROM signature_table")
    public LiveData<List<SignatureEntity>> getSignature();

    @Query("SELECT * FROM mechanic_signature_table")
    public LiveData<List<MechanicSignatureEntity>> getMechanicSignature();

}
