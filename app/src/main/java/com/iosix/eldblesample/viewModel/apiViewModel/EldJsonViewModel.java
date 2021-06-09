package com.iosix.eldblesample.viewModel.apiViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.iosix.eldblesample.models.SendExampleModelData;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.retrofit.ApiRepository;

public class EldJsonViewModel extends AndroidViewModel {
    private ApiRepository repository;

    public EldJsonViewModel(@NonNull Application application) {
        super(application);
        repository = new ApiRepository(application);
    }

    public void sendData(SendExampleModelData data) {
        repository.sendEldData(data);
    }
}
