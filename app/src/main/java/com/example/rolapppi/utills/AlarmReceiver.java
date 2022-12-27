package com.example.rolapppi.utills;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.rolapppi.R;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        Intent i = new Intent(context, DestinationActivity.class)

        String name = intent.getStringExtra("NAME");
        int requestCode = intent.getIntExtra("REQUEST_CODE",-1);

        NotificationChannel channel = new NotificationChannel("NOTIFY", "NOTIFY", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

//        Toast.makeText(context,"dd "+requestCode, Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "NOTIFY")
                .setSmallIcon(R.drawable.cow)
                .setContentTitle("Rollappi Przypomina " + requestCode)
                .setContentText("Czas na zaszuszenie krowy: " + name)
                .setAutoCancel(true)
                .setLights(Color.RED, 500, 500)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(requestCode, builder.build());

    }
}
