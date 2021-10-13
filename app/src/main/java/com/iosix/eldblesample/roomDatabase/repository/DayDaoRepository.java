package com.iosix.eldblesample.roomDatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.roomDatabase.daos.DayDao;
import com.iosix.eldblesample.roomDatabase.daos.StatusTruckDao;
import com.iosix.eldblesample.roomDatabase.database.StatusTruckRoomDatabase;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DayDaoRepository {
    private final DayDao dayDao;
    private final LiveData<List<DayEntity>> getAllDays;
    private final LiveData<List<LogEntity>> mAllStatus;
    private final LiveData<List<VehiclesEntity>> getAllVehicles;
    private final LiveData<List<TrailersEntity>> getAllTrailers;


    public DayDaoRepository(Application application) {
        StatusTruckRoomDatabase db = StatusTruckRoomDatabase.getINSTANCE(application);
        dayDao = db.dayDao();
        getAllDays = dayDao.getAllDays();
        mAllStatus = dayDao.getAllStatus();
        getAllVehicles = dayDao.getAllVehicles();
        getAllTrailers = dayDao.getAllTrailers();
    }

    public LiveData<List<DayEntity>> getGetAllDays() {
        return getAllDays;
    }

    public LiveData<List<TrailersEntity>> getGetAllTrailers() {
        return getAllTrailers;
    }

    public LiveData<List<VehiclesEntity>> getGetAllVehicles() {
        return getAllVehicles;
    }

    public Long insertDay(DayEntity dayEntity) throws ExecutionException, InterruptedException {
        return new insertDayAsync(dayDao).execute(dayEntity).get();
    }

    public void deleteDay(DayEntity entity) {
        new deleterDayAsync(dayDao).execute(entity);
    }
    public void deleteAllDays() {
        new deleteAllDaysAsync(dayDao).execute();
    }

    public LiveData<List<LogEntity>> getmAllStatus() {
        return mAllStatus;
    }

    public void insertStatus(LogEntity entity) {
        new DayDaoRepository.insertStatusAsync(dayDao).execute(entity);
    }

    public void insertVehicle(VehiclesEntity entity) {
        new insertVehicleAsync(dayDao).execute(entity);
    }

    public Long insertTrailer(TrailersEntity entity) throws ExecutionException, InterruptedException {
        return new insertTrailerAsycn(dayDao).execute(entity).get();
    }

    @SuppressWarnings("deprecation")
    private static class insertDayAsync extends AsyncTask<DayEntity, Void, Long> {
        private DayDao dao;

        insertDayAsync(DayDao dayDao) {
            this.dao = dayDao;
        }

        @Override
        protected Long doInBackground(DayEntity... dayEntities) {

            return dao.insertDay(dayEntities[0]);
        }
    }

    private static class insertTrailerAsycn extends AsyncTask<TrailersEntity, Void, Long> {
        private DayDao dayDao;

        insertTrailerAsycn(DayDao dayDao) {
            this.dayDao = dayDao;
        }


        @Override
        protected Long doInBackground(TrailersEntity... trailersEntities) {
            return dayDao.insertTrailer(trailersEntities[0]);
        }
    }

    private static class insertVehicleAsync extends AsyncTask<VehiclesEntity, Void, Void> {
        private DayDao dao;

        insertVehicleAsync(DayDao dayDao) {
            this.dao = dayDao;
        }

        @Override
        protected Void doInBackground(VehiclesEntity... vehiclesEntities) {
            dao.insertVehicle(vehiclesEntities[0]);
            return null;
        }
    }

    private static class deleterDayAsync extends AsyncTask<DayEntity, Void, Long> {
        private DayDao dao;

        deleterDayAsync(DayDao dayDao) {
            this.dao = dayDao;
        }

        @Override
        protected Long doInBackground(DayEntity... dayEntities) {
            dao.deleteDay(dayEntities[0]);
            return null;
        }
    }

    private static class deleteAllDaysAsync extends AsyncTask<DayEntity, Void, Long> {
        private DayDao dao;

        deleteAllDaysAsync(DayDao dayDao) {
            this.dao = dayDao;
        }

        @Override
        protected Long doInBackground(DayEntity... dayEntities) {
            dao.deleteAllDay();
            return null;
        }
    }

    private static class insertStatusAsync extends AsyncTask<LogEntity, Void, Void> {
        private DayDao dao;

        insertStatusAsync(DayDao statusTruckDao) {
            dao = statusTruckDao;
        }

        @Override
        protected Void doInBackground(LogEntity... truckStatusEntities) {
            dao.insertStatus(truckStatusEntities[0]);
            return null;
        }
    }
}
