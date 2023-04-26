package com.iosix.eldblesample.roomDatabase.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.iosix.eldblesample.models.Dvir;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.VehicleList;
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
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.MainOfficeEntity;
import com.iosix.eldblesample.roomDatabase.entities.MechanicSignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailerDefectsEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailerId;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.roomDatabase.entities.UnitDefectsEntity;

@Database(entities = {Status.class, DayEntity.class, Dvir.class, GeneralEntity.class,
        TrailersEntity.class, TrailerDefectsEntity.class,
        UnitDefectsEntity.class, VehicleList.class,
        MainOfficeEntity.class, SignatureEntity.class,
        MechanicSignatureEntity.class, User.class, TrailerId.class, LiveDataRecord.class,
},exportSchema = false, version = 50)
@TypeConverters({Converter.class, TrailerConverter.class,TrailerConverterString.class})
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
                        .build();
            }
        }
        return INSTANCE;
    }

}
