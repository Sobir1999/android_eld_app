package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.roomDatabase.daos.DayDao;
import com.iosix.eldblesample.roomDatabase.daos.UserDao;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;

import java.util.List;

public class UserRepository {

    private final UserDao userDao;
    private final LiveData<User> getUser;
    private final LiveData<List<User>> getDrivers;
    private final LiveData<List<LiveDataRecord>> getLocalDatas;


    public UserRepository(Application application) {
        StatusTruckRoomDatabase db = StatusTruckRoomDatabase.getINSTANCE(application);
        userDao = db.userDao();
        getUser = userDao.getUser();
        getDrivers = userDao.getDrivers();
        getLocalDatas = userDao.getLocalDatas();
    }

    public LiveData<User> getUser() {
        return getUser;
    }

    public void insertUser(User user) {
        new UserRepository.insertUserAsync(userDao).execute(user);
    }

    public LiveData<List<User>> getDrivers() {
        return getDrivers;
    }

    public void deleteUser() {
        new UserRepository.deleteUserAsync(userDao).execute();
    }

    public LiveData<List<LiveDataRecord>> getLocalDatas(){ return getLocalDatas;}

    public void insertLocalData(LiveDataRecord liveDataRecord){new UserRepository.insertLocalDataAsync(userDao).execute(liveDataRecord);}

    public void deleteLocalData(){new UserRepository.deleteLocalDataAsync(userDao).execute();}

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

    private static class insertLocalDataAsync extends AsyncTask<LiveDataRecord, Void, Void> {
        private UserDao userDao;

        insertLocalDataAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(LiveDataRecord... liveDataRecords) {
            userDao.insertLocalData(liveDataRecords[0]);
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

    private static class deleteLocalDataAsync extends AsyncTask<LiveDataRecord, Void, Void> {
        private UserDao userDao;

        deleteLocalDataAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(LiveDataRecord... liveDataRecords) {
            userDao.deletLocalDatas();
            return null;
        }
    }
}
