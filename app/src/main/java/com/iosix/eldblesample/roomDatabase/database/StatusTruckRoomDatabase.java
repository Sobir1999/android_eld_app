package com.iosix.eldblesample.roomDatabase.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.iosix.eldblesample.roomDatabase.daos.DayDao;
import com.iosix.eldblesample.roomDatabase.daos.StatusTruckDao;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.TruckStatusEntity;

@Database(entities = {TruckStatusEntity.class, DayEntity.class}, version = 5)
public abstract class StatusTruckRoomDatabase extends RoomDatabase {

    public abstract StatusTruckDao statusTruckDao();
    public abstract DayDao dayDao();

    private static StatusTruckRoomDatabase INSTANCE;

    public static synchronized StatusTruckRoomDatabase getINSTANCE(final Context context) {
        synchronized (StatusTruckRoomDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StatusTruckRoomDatabase.class, "status_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private StatusTruckDao dao;
        private DayDao dayAndStatusDao;

        private PopulateDBAsyncTask(StatusTruckRoomDatabase db) {
            dao = db.statusTruckDao();
            dayAndStatusDao = db.dayDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            dao.insertStatus(new TruckStatusEntity(0, "Tashkent", "Qale Toshkent", null, "Apr 27" + 2021));
//            dayAndStatusDao.insertDay(new DayEntity("Today"));
            return null;
        }
    }

}
