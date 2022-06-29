package com.iosix.eldblesample.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.repository.DvirRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DvirViewModel extends AndroidViewModel {

    private DvirRepository dvirRepository;
    private LiveData<List<DvirEntity>> mgetDvirs;

    public DvirViewModel(@NonNull Application application) {
        super(application);
        dvirRepository = new DvirRepository(application);
        mgetDvirs = dvirRepository.getAllDvirs();
    }

    private MutableLiveData<String> currentName;
    private MutableLiveData<Boolean> isUploadImage;
    private MutableLiveData<Boolean> truckSpeed;
    private MutableLiveData<Integer> selectedTrailerCount;
    private MutableLiveData<Integer> getEldConnectionState;

    public MutableLiveData<String> getCurrentName() {
        if (currentName == null) {
            currentName = new MutableLiveData<String>();
        }
        return currentName;
    }

    public MutableLiveData<Integer> getEldConnectionState() {
        if (getEldConnectionState == null) {
            getEldConnectionState = new MutableLiveData<>();
            getEldConnectionState.postValue(0);
        }
        return getEldConnectionState;
    }

    public MutableLiveData<Integer> getSelectedTrailerCount() {
        if (selectedTrailerCount == null) {
            selectedTrailerCount = new MutableLiveData<Integer>();
            selectedTrailerCount.postValue(0);
        }
        return selectedTrailerCount;
    }

    public MutableLiveData<Boolean> getTruckSpeed() {
        if (truckSpeed == null) {
            truckSpeed = new MutableLiveData<>();
            truckSpeed.postValue(false);
        }
        return truckSpeed;
    }

    public LiveData<List<DvirEntity>> getMgetDvirs() {
        return mgetDvirs;
    }

    public void deleteDvir(DvirEntity entity) {
        dvirRepository.deleteDvir(entity);
    }


    public Long insertDvir(DvirEntity dvirEntity) throws ExecutionException, InterruptedException {
        return dvirRepository.insertDvir(dvirEntity);
    }

}
