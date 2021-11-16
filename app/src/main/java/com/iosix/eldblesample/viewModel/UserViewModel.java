package com.iosix.eldblesample.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;
import com.iosix.eldblesample.roomDatabase.repository.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;
    private final LiveData<User> mgetUser;

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
        mgetUser = repository.getUser();
    }

    public LiveData<User> getMgetUser() {
        return mgetUser;
    }

    public void insertUser(User user) {
        repository.insertUser(user);
    }

    public void deleteUser() {
        repository.deleteUser();
    }
}
