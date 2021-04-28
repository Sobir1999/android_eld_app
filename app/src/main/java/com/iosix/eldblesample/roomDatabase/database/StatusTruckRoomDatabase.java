package com.iosix.eldblesample.roomDatabase.database;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.iosix.eldblesample.roomDatabase.daos.StatusTruckDao;
import com.iosix.eldblesample.roomDatabase.entities.TruckStatusEntity;

@Database(entities = {TruckStatusEntity.class}, version = 1)
public abstract class StatusRoomDatabase extends RoomDatabase {

    public abstract StatusTruckDao statusTruckDao();
    private static StatusRoomDatabase Instance;

    public static synchronized StatusRoomDatabase getINSTANCE(final Context context) {
        synchronized (StatusRoomDatabase.class) {
            if (Instance == null) {
                Room.databaseBuilder(context.getApplicationContext(), StatusRoomDatabase.class, "status_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
            }
        }
        return Instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(Instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private StatusTruckDao dao;

        private PopulateDBAsyncTask(StatusRoomDatabase db) {
            dao = db.statusTruckDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.insertStatus(new TruckStatusEntity(0, "OFF", "Tashkent", "Qale Toshkent", null, 120, "Apr 27", 2021));
            return null;
        }
    }

}
