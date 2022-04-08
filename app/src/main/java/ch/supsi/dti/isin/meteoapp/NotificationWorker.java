package ch.supsi.dti.isin.meteoapp;


import static java.lang.Thread.sleep;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ch.supsi.dti.isin.meteoapp.activities.MainActivity;
import ch.supsi.dti.isin.meteoapp.fragments.DetailLocationFragment;
import ch.supsi.dti.isin.meteoapp.model.Location;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class NotificationWorker extends Worker{

    private Context mContext;
    Location mLocation;
    private Context mainContext;
    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
        mLocation = new Location();
        try {
            sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mainContext = MainActivity.context;
        if (ContextCompat.checkSelfPermission(mainContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            startLocationListener();
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        //Sleep cosi si setuppa tutto i dati, altrimenti ritorna null

        try {
            sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(mLocation == null){
            mLocation = new Location("Roma","Italia");
        }

        DetailLocationFragment instanza = DetailLocationFragment.newInstance(mLocation.getId(),mLocation.getName(),mLocation.getCountry());

        instanza.setInformation(mContext,1);
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(instanza.getTempAttuale());


        sendNotification(mLocation.getName(), instanza.getTempAttuale().substring(0,4));

        return Result.success();
    }

    private void sendNotification(String location,String temperatura) {
        NotificationCompat.Builder mBuilder = mBuilder = new NotificationCompat.Builder(mContext, "default")
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle("NOTIFICA METEO")
                    .setContentText("Attualmente a "+location+" ci sono "+temperatura+" gradi")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat manager = NotificationManagerCompat.from(mContext);
        manager.notify(0, mBuilder.build());
    }

    public void startLocationListener() {
        long mLocTrackingInterval = 1000 * 60; // 5 sec
        float trackingDistance = 0;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy)
                .setDistance(trackingDistance)
                .setInterval(mLocTrackingInterval);

        SmartLocation.with(mContext)
                .location()
                .continuous()
                .config(builder.build())
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(android.location.Location location) {

                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        try {
                            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);

                            String addressComplete = addresses.get(0).getAddressLine(0);
                            String[] addressSplit = addressComplete.split(",");

                            String countryName;
                            String[] cityPlusZipCode;
                            //TODO CHECK IF QUA CI SONO LUNGHEZZA DIVERSE
                            if(addressSplit.length == 3){
                                countryName = addressSplit[2].trim();
                                cityPlusZipCode = addressSplit[1].split(" ");
                            }else {
                                countryName = addressSplit[3].trim();
                                cityPlusZipCode = addressSplit[2].split(" ");
                            }
                            String cityName = cityPlusZipCode[2].trim();
                            mLocation.setName(cityName);
                            mLocation.setCountry(countryName);

                            return;
                        } catch (IOException ex) {

                        }
                    }
                });
    }
}
