package com.iosix.eldblesample.roomDatabase.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.roomDatabase.converter.Converter;
import com.iosix.eldblesample.roomDatabase.converter.TrailerConverter;
import com.iosix.eldblesample.roomDatabase.converter.TrailerConverterString;
import com.iosix.eldblesample.roomDatabase.daos.DVIRDao;
import com.iosix.eldblesample.roomDatabase.daos.DayDao;
import com.iosix.eldblesample.roomDatabase.daos.GeneralDao;
import com.iosix.eldblesample.roomDatabase.daos.SignatureDao;
import com.iosix.eldblesample.roomDatabase.daos.StatusTruckDao;
import com.iosix.eldblesample.roomDatabase.daos.UserDao;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.roomDatabase.entities.MainOfficeEntity;
import com.iosix.eldblesample.roomDatabase.entities.MechanicSignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailerDefectsEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailerId;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.roomDatabase.entities.UnitDefectsEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;

@Database(entities = {LogEntity.class, DayEntity.class, DvirEntity.class, GeneralEntity.class,
        TrailersEntity.class, TrailerDefectsEntity.class,
        UnitDefectsEntity.class, VehiclesEntity.class,
        MainOfficeEntity.class, SignatureEntity.class,
        MechanicSignatureEntity.class, User.class, TrailerId.class, LiveDataRecord.class,
}, version = 30)
@TypeConverters({Converter.class, TrailerConverter.class, TrailerConverterString.class})
public abstract class StatusTruckRoomDatabase extends RoomDatabase {

    public abstract StatusTruckDao statusTruckDao();
    public abstract DayDao dayDao();
    public abstract SignatureDao getSignatureDao();
    public abstract DVIRDao dvirDao();
    public abstract UserDao userDao();
    public abstract GeneralDao generalDao();

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
            return null;
        }
    }

}
