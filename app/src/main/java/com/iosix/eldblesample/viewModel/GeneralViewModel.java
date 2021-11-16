package com.iosix.eldblesample.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.repository.DvirRepository;
import com.iosix.eldblesample.roomDatabase.repository.GeneralRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GeneralViewModel extends AndroidViewModel {

    private GeneralRepository generalRepository;
    private LiveData<List<GeneralEntity>> mgetGenerals;

    public GeneralViewModel(@NonNull Application application) {
        super(application);
        generalRepository = new GeneralRepository(application);
        mgetGenerals = generalRepository.getGeneral();
    }

    public LiveData<List<GeneralEntity>> getMgetGenerals() {
        return mgetGenerals;
    }

    public void deleteGeneral(GeneralEntity entity) {
        generalRepository.deleteGeneral(entity);
    }


    public Long insertGeneral(GeneralEntity entity) throws ExecutionException, InterruptedException {
        return generalRepository.insertGeneral(entity);
    }
}
