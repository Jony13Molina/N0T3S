package com.example.jonny.n0t3s.Main;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
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

import java.util.Random;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingSevice extends FirebaseMessagingService {



    private final String CHANNEL_ID = "channel_id";


    //generating new token for the messaging
    /*@Override
    public void onNewToken(String msgToken){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                    }
                });
        Log.i(TAG, "this is the token called from onNewToken"+msgToken);
    }*/

    //----------This Section is where the Messaging service functions take action--------------\\
    @Override
    public void onMessageReceived(RemoteMessage msgRemote){
        final Intent myIntent = new Intent(this, MainActivity.class);
        NotificationManager myNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        //generating the msgID
        int msgNotiID = new Random().nextInt(3000);



        //check of the SDK version is 26 or higher
        //if it is we need to set up the channel else no need to set up channel

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setNotiChannel(myNotificationManager);
        }

        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0, myIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Bitmap notiIcon= BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_notificationlogo);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notificationlogo)
                .setLargeIcon(notiIcon)
                .setContentTitle(msgRemote.getData().get("title"))
                .setContentText(msgRemote.getData().get("message"))
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(),
                    R.color.cadetBlue));
        }
        myNotificationManager.notify(msgNotiID, notificationBuilder.build());


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