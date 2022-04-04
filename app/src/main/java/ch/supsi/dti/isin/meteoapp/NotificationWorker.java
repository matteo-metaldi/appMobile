package ch.supsi.dti.isin.meteoapp;

import static androidx.core.content.ContextCompat.getSystemService;

import static java.lang.Thread.sleep;

import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.time.LocalTime;

import ch.supsi.dti.isin.meteoapp.activities.MainActivity;
import ch.supsi.dti.isin.meteoapp.fragments.DetailLocationFragment;
import ch.supsi.dti.isin.meteoapp.model.Location;

public class NotificationWorker extends Worker{

    private Context mContext;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        //Sleep cosi si setuppa tutto i dati, altrimenti ritorna null
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Ottengo la location attuale
        Location location = MainActivity.getCurrentLocation();

        if(location.getName() == null){
            location.setCountry("Italy");
            location.setName("Rome");
        }

        DetailLocationFragment instanza = DetailLocationFragment.newInstance(location.getId(),location.getName(),location.getCountry());

        instanza.setInformation(mContext,1);
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(instanza.getTempAttuale());


        sendNotification(location.getName(), instanza.getTempAttuale().substring(0,4));

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


}
