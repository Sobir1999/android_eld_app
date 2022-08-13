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
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserRepository {

    private final UserDao userDao;
    private final LiveData<User> getUser;
    private final LiveData<List<User>> getDrivers;
    private final LiveData<List<LiveDataRecord>> getLocalDatas;
    private final LiveData<List<VehiclesEntity>> getAllVehicles;
    private final LiveData<List<TrailersEntity>> getAllTrailers;


    public UserRepository(Application application) {
        StatusTruckRoomDatabase db = StatusTruckRoomDatabase.getINSTANCE(application);
        userDao = db.userDao();
        getUser = userDao.getUser();
        getDrivers = userDao.getDrivers();
        getLocalDatas = userDao.getLocalDatas();
        getAllVehicles = userDao.getAllVehicles();
        getAllTrailers = userDao.getAllTrailers();
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

    public LiveData<List<TrailersEntity>> getGetAllTrailers() {
        return getAllTrailers;
    }

    public LiveData<List<VehiclesEntity>> getGetAllVehicles() {
        return getAllVehicles;
    }

    public void insertVehicle(VehiclesEntity entity) {
        new insertVehicleAsync(userDao).execute(entity);
    }

    public Long insertTrailer(TrailersEntity entity) throws ExecutionException, InterruptedException {
        return new insertTrailerAsycn(userDao).execute(entity).get();
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


    private static class insertTrailerAsycn extends AsyncTask<TrailersEntity, Void, Long> {
        private UserDao dayDao;

        insertTrailerAsycn(UserDao dayDao) {
            this.dayDao = dayDao;
        }


        @Override
        protected Long doInBackground(TrailersEntity... trailersEntities) {
            return dayDao.insertTrailer(trailersEntities[0]);
        }
    }

    private static class insertVehicleAsync extends AsyncTask<VehiclesEntity, Void, Void> {
        private UserDao dao;

        insertVehicleAsync(UserDao dayDao) {
            this.dao = dayDao;
        }

        @Override
        protected Void doInBackground(VehiclesEntity... vehiclesEntities) {
            dao.insertVehicle(vehiclesEntities[0]);
            return null;
        }
    }
}
