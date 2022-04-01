package ch.supsi.dti.isin.meteoapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "location")
public class Location {

    @PrimaryKey
    @NonNull
    private UUID Id;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "country")
    private String mCountry;

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public Location() {
        Id = UUID.randomUUID();
    }

    @Ignore
    public Location(String nmae,String country) {
        mName = nmae;
        mCountry = country;
        Id = UUID.randomUUID();
    }

}