package ch.supsi.dti.isin.meteoapp.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {Location.class}, version = 1, exportSchema = false)
@TypeConverters({UUIDConverter.class})
public abstract class LocationDatabase extends RoomDatabase{

    private static final String DATABASE_NAME = "location_db";
    private static LocationDatabase sInstance;

    public static LocationDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), LocationDatabase.class, LocationDatabase.DATABASE_NAME).build();
        }
        return sInstance;
    }

    public abstract LocationDao locationDao();


    //Metodi fatti da lui in automatico
    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
