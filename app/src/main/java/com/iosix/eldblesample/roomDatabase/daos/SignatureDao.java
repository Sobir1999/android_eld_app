package com.iosix.eldblesample.roomDatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;

import java.util.List;

@Dao
public interface SignatureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertSignature(SignatureEntity signature);

    @Query("SELECT * FROM signature_table")
    public LiveData<List<SignatureEntity>> getSignature();

}
