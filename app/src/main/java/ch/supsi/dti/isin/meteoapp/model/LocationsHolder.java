package ch.supsi.dti.isin.meteoapp.model;

import static java.lang.Thread.sleep;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.supsi.dti.isin.meteoapp.fragments.ListFragment;

public class LocationsHolder {

    //todo qua aggiunge il nome alla lista

    private LocationDatabase db;
    private static LocationsHolder sLocationsHolder;
    private List<Location> mLocations;

    public static LocationsHolder get(Context context) {
        if (sLocationsHolder == null)
            sLocationsHolder = new LocationsHolder(context);

        return sLocationsHolder;
    }

    private LocationsHolder(Context context) {
        db = LocationDatabase.getInstance(context);
        new Thread(()->mLocations = db.locationDao().getLocations()).start();
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public List<Location> getLocations() {
        return mLocations;
    }

    public Location getLocation(UUID id) {
        for (Location location : mLocations) {
            if (location.getId().equals(id))
                return location;
        }

        return null;
    }

    public void addLocationToList(Location location){
        mLocations.add(location);
        System.out.println(location.getName());

    }
}
