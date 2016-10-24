package com.example.michael.myapplication;

/**
 * Created by michael on 10/22/16.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class CourseNotifyService extends Service {


    //Notification mNotify;
    String title = "";
    String content = "";

    @Override
    public IBinder onBind(Intent arg0) {
        arg0.getExtras();
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        //NotificationManagerCompat mNM = (NotificationManagerCompat)getSystemService(NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification mNotify = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_menu_manage)
                .setContentIntent(pIntent)
                .setSound(sound)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, mNotify);
        return PendingIntent.FLAG_UPDATE_CURRENT;
    }
}