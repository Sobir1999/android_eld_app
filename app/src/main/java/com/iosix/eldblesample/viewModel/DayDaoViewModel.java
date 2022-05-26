package com.iosix.eldblesample.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;
import com.iosix.eldblesample.roomDatabase.repository.DayDaoRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DayDaoViewModel extends AndroidViewModel {
    private final DayDaoRepository repository;
    private final LiveData<List<DayEntity>> mgetAllDays;
    private final LiveData<List<VehiclesEntity>> getAllVehicles;
    private final LiveData<List<TrailersEntity>> getAllTrailers;
    private LiveData<List<LogEntity>> mAllStatus;


    public DayDaoViewModel(@NonNull Application application) {
        super(application);
        repository = new DayDaoRepository(application);
        mgetAllDays = repository.getGetAllDays();
        getAllVehicles = repository.getGetAllVehicles();
        getAllTrailers = repository.getGetAllTrailers();
        mAllStatus = repository.getmAllStatus();
    }

    public LiveData<List<TrailersEntity>> getGetAllTrailers() {
        return getAllTrailers;
    }

    public LiveData<List<DayEntity>> getMgetAllDays() {
        return mgetAllDays;
    }

    public void insertDay(DayEntity entity) throws ExecutionException, InterruptedException {
        repository.insertDay(entity);
    }

    public void deleteDay(DayEntity entity) {
        repository.deleteDay(entity);
    }
    public void deleteAllDays() {
        repository.deleteAllDays();
    }

    public void insertVehicle(VehiclesEntity vehiclesEntity) {
        repository.insertVehicle(vehiclesEntity);
    }

    public LiveData<List<VehiclesEntity>> getGetAllVehicles() {return getAllVehicles;}

    public void insertTrailer(TrailersEntity entity) throws ExecutionException, InterruptedException {
        repository.insertTrailer(entity);
    }

    public LiveData<List<LogEntity>> getmAllStatus() {
        return mAllStatus;
    }

    public void insertStatus(LogEntity statusEntity) throws ExecutionException, InterruptedException {
        repository.insertStatus(statusEntity);
    }
}
