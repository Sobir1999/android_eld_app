package com.iosix.eldblesample.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.roomDatabase.repository.StatusTableRepositories;

import java.util.List;

public class StatusDaoViewModel extends AndroidViewModel {
    private StatusTableRepositories repository;
    private LiveData<List<LogEntity>> mAllStatus;

    public StatusDaoViewModel(Application application) {
        super(application);
        repository = new StatusTableRepositories(application);
        mAllStatus = repository.getmAllStatus();
    }

    public LiveData<List<LogEntity>> getmAllStatus() {
        return mAllStatus;
    }

    public void insertStatus(LogEntity statusEntity) {
        repository.insertStatus(statusEntity);
    }

    public void deleteALlStatus() {
//        repository
    }
}
