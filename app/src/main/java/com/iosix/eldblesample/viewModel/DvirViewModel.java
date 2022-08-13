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
    private MutableLiveData<String> stringMutableLiveData;
    private MutableLiveData<Integer> vehiclesMutableLiveData;
    private MutableLiveData<Integer> coDriversMutableLiveData;
    private MutableLiveData<Integer> trailersMutableLiveData;
    private MutableLiveData<Integer> shippingDocsMutableLiveData;

    public DvirViewModel(@NonNull Application application) {
        super(application);
        dvirRepository = new DvirRepository(application);
        mgetDvirs = dvirRepository.getAllDvirs();
    }

    private MutableLiveData<String> currentName;

    public MutableLiveData<String> getCurrentName() {
        if (currentName == null) {
            currentName = new MutableLiveData<String>();
        }
        return currentName;
    }

    public MutableLiveData<String> getStringMutableLiveData() {
        if (stringMutableLiveData == null) {
            stringMutableLiveData = new MutableLiveData<String>();
        }
        return stringMutableLiveData;
    }

    public MutableLiveData<Integer> getVehiclesMutableLiveData() {
        if (vehiclesMutableLiveData == null) {
            vehiclesMutableLiveData = new MutableLiveData<>();
        }
        return vehiclesMutableLiveData;
    }

    public MutableLiveData<Integer> getCoDriversMutableLiveData() {
        if (coDriversMutableLiveData == null) {
            coDriversMutableLiveData = new MutableLiveData<>();
        }
        return coDriversMutableLiveData;
    }

    public MutableLiveData<Integer> getTrailersMutableLiveData() {
        if (trailersMutableLiveData == null) {
            trailersMutableLiveData = new MutableLiveData<>();
        }
        return trailersMutableLiveData;
    }

    public MutableLiveData<Integer> getShippingDocsMutableLiveData() {
        if (shippingDocsMutableLiveData == null) {
            shippingDocsMutableLiveData = new MutableLiveData<>();
        }
        return shippingDocsMutableLiveData;
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
