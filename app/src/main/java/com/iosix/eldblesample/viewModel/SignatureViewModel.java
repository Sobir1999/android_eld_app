package com.iosix.eldblesample.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.roomDatabase.repository.DayDaoRepository;
import com.iosix.eldblesample.roomDatabase.repository.SignatureRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SignatureViewModel extends AndroidViewModel {

    private SignatureRepository repository;
    private LiveData<List<SignatureEntity>> mgetAllSignatures;

    public SignatureViewModel(@NonNull Application application) {
        super(application);
        repository = new SignatureRepository(application);
        mgetAllSignatures = repository.getGetAllSignatures();
    }

    public LiveData<List<SignatureEntity>> getMgetAllSignatures() {
        return mgetAllSignatures;
    }


    public Long insertSignature(SignatureEntity entity) throws ExecutionException, InterruptedException {
        return repository.insertSignature(entity);
    }
}
