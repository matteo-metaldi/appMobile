package ch.supsi.dti.isin.meteoapp.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM location")
    List<Location> getLocations();

    @Insert
    void insertLocation(Location location);

    @Delete
    void deleteLocation(Location location);
}
