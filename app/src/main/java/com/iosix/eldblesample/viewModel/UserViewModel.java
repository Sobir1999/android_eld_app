package com.iosix.eldblesample.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;
import com.iosix.eldblesample.roomDatabase.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;
    private final LiveData<User> mgetUser;
    private final LiveData<List<User>> mgetDrivers;
    private final LiveData<List<LiveDataRecord>> mgetLocalDatas;

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
        mgetUser = repository.getUser();
        mgetDrivers = repository.getDrivers();
        mgetLocalDatas = repository.getLocalDatas();
    }

    public LiveData<User> getMgetUser() {
        return mgetUser;
    }
    public LiveData<List<User>> getMgetDrivers() {
        return mgetDrivers;
    }

    public LiveData<List<LiveDataRecord>> getMgetLocalDatas(){return mgetLocalDatas;}

    public void insertUser(User user) {
        repository.insertUser(user);
    }

    public void insertLocalData(LiveDataRecord liveDataRecord){repository.insertLocalData(liveDataRecord);}

    public void deleteUser() {
        repository.deleteUser();
    }

    public void deleteLocalData(){repository.deleteLocalData();}
}
