package com.example.jonny.n0t3s.Main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.jonny.n0t3s.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingSevice extends FirebaseMessagingService {




    Map<String, String> myData = new HashMap<>();
    private final String CHANNEL_ID = "channel_id";



    @Override
    public void onMessageReceived(RemoteMessage msgRemote){
        final Intent myIntent = new Intent(this, MainActivity.class);

        //generating the msgID
        int msgNotiID = new Random().nextInt(3000);



        //check of the SDK version is 26 or higher
        //if it is we need to set up the channel else no need to set up channel



        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0, myIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap notiIcon= BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_notificationlogo);



        myData = msgRemote.getData();
        String title = myData.get("title");
        String msg = myData.get("message");

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notificationlogo)
                .setLargeIcon(notiIcon)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(),
                    R.color.cadetBlue));
        }
        NotificationManager myNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        myNotificationManager.notify(msgNotiID, notificationBuilder.build());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setNotiChannel(myNotificationManager);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNotiChannel(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to device notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }


}



