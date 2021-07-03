package com.iosix.eldblesample.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.repository.DayDaoRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DayDaoViewModel extends AndroidViewModel {
    private DayDaoRepository repository;
    private LiveData<List<DayEntity>> mgetAllDays;

    public DayDaoViewModel(@NonNull Application application) {
        super(application);
        repository = new DayDaoRepository(application);
        mgetAllDays = repository.getGetAllDays();
    }

    public LiveData<List<DayEntity>> getMgetAllDays() {
        return mgetAllDays;
    }

    public Long insertDay(DayEntity entity) throws ExecutionException, InterruptedException {
        return repository.insertDay(entity);
    }

    public void deleteDay(DayEntity entity) {
        repository.deleteDay(entity);
    }
}
