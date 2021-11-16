package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.roomDatabase.daos.DayDao;
import com.iosix.eldblesample.roomDatabase.daos.UserDao;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;

import java.util.List;

public class UserRepository {

    private final UserDao userDao;
    private final LiveData<User> getUser;


    public UserRepository(Application application) {
        StatusTruckRoomDatabase db = StatusTruckRoomDatabase.getINSTANCE(application);
        userDao = db.userDao();
        getUser = userDao.getUser();
    }

    public LiveData<User> getUser() {
        return getUser;
    }

    public void insertUser(User user) {
        new UserRepository.insertUserAsync(userDao).execute(user);
    }

    public void deleteUser() {
        new UserRepository.deleteUserAsync(userDao).execute();
    }

    private static class insertUserAsync extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        insertUserAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insertUser(users[0]);
            return null;
        }
    }

    private static class deleteUserAsync extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        deleteUserAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.deleteUser();
            return null;
        }
    }
}
