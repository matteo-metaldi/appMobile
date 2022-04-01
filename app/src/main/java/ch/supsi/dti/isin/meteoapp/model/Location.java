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

    public Location() {
        Id = UUID.randomUUID();
    }

    @Ignore
    public Location(String nmae) {
        mName = nmae;
        Id = UUID.randomUUID();
    }
}