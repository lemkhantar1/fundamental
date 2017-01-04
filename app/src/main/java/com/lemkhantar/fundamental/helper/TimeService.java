package com.lemkhantar.fundamental.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.activities.Historique;
import com.lemkhantar.fundamental.database.DBManager;
import com.lemkhantar.fundamental.entity.OperationAuto;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lemkhantar1 on 7/15/16.
 */
public class TimeService extends Service {

    // constant
    public static final long NOTIFY_INTERVAL =  1000; // 1 second

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();

    // timer handling
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL); // cancel if already existed

    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                    if(getDateTime().equals("000000"))
                    {
                        // ajout d'un debit de X dirhams;
                        DBManager dbManager = new DBManager(getApplicationContext());
                        dbManager.openDataBase();
                        OperationAuto operationAuto = dbManager.getOperationAuto(Constantes.OPERATIO_AUTO_1);
                        if(operationAuto.get_active()==1)
                        {
                            dbManager.executerOperationAuto(Constantes.OPERATIO_AUTO_1);
                            // notifications
                            createNotification();
                        }

                    }
                }
            });
        }

        private String getDateTime() {
            // get date time in custom format
            final Calendar c = Calendar.getInstance();
            String heure = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            String minute = String.valueOf(c.get(Calendar.MINUTE));
            String seconde = String.valueOf(c.get(Calendar.SECOND));
            if(heure.length()==1)heure="0"+heure;
            if(minute.length()==1)minute="0"+minute;
            if(seconde.length()==1)seconde="0"+seconde;
            return heure+minute+seconde;
        }


    }


    public void onDestroy()
    {
        mTimer.cancel();
        super.onDestroy();
    }


    public final void createNotification()
    {

        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, Historique.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(r.getString(R.string.notification_title))
                .setSmallIcon(R.drawable.dollar)
                .setContentTitle("TAXI 7247")
                .setContentText("Nouvelle operation !")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }


}
