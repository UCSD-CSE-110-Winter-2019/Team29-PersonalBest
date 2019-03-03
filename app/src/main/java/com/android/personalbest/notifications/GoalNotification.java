package com.android.personalbest.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.android.personalbest.R;

public class GoalNotification implements INotification {

    Context context;

    public GoalNotification(Context context) {
        this.context = context;
    }

    public void sendNotif(String title, String body) {
        NotificationManager notif=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder
                (context).setContentTitle(title).setContentText(body).build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }
}
