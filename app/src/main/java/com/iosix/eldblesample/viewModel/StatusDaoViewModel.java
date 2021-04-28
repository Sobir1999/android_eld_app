package com.iosix.eldblesample.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.entities.TruckStatusEntity;
import com.iosix.eldblesample.roomDatabase.repository.StatusTableRepositories;

import java.util.List;

public class StatusDaoViewModel extends AndroidViewModel {
    private StatusTableRepositories repository;
    private LiveData<List<TruckStatusEntity>> mAllStatus;

    public StatusDaoViewModel(Application application) {
        super(application);
        repository = new StatusTableRepositories(application);
        mAllStatus = repository.getmAllStatus();
    }

    public LiveData<List<TruckStatusEntity>> getmAllStatus() {
        return mAllStatus;
    }

    public void insertStatus(TruckStatusEntity statusEntity) {
        repository.insertStatus(statusEntity);
    }

    public void deleteALlStatus() {
//        repository
    }
}
