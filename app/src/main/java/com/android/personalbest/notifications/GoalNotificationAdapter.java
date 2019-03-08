package com.android.personalbest.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.android.personalbest.MainPageActivity;
import com.android.personalbest.R;

public class GoalNotificationAdapter implements INotification {

    Context context;
    NotificationManager notificationManager;
    NotificationChannel notificationChannel;
    NotificationCompat.Builder builder;
    Intent intent;
    PendingIntent pendingIntent;

    public GoalNotificationAdapter(Context context) {
        this.context = context;
        setupNotif();
    }

    public void sendNotif(String title, String body) {
        builder = builder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(0, builder.build());
    }

    private void setupNotif() {
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationChannel = new NotificationChannel("ID", "Personal Best", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(notificationChannel);
        builder = new NotificationCompat.Builder(context, notificationChannel.getId());
        intent = new Intent(context, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    }
}
