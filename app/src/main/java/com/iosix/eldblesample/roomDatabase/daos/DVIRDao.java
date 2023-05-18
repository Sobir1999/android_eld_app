package com.iosix.eldblesample.roomDatabase.daos;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.iosix.eldblesample.models.Dvir;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface DVIRDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDvir(Dvir dvirEntity);


    @Query("SELECT * FROM dvir_table")
    Single<List<Dvir>> getDvirs();

    @Query("SELECT * FROM dvir_table")
    Single<List<Dvir>> getCurrDateDvirs();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable update(Dvir dvir);

    @Delete
    Completable deleteDvir(Dvir dvirEntity);

}
