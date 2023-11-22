package com.example.ProjectCC05;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    /*
        If the user checks the setReminder checkbox, it creates an AlarmManager,
        and NotificationReceiver receives it. When the specified date that the user
        inputted in the date field comes, the things in the .builder will show on the notification.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        long dueDateMillis = intent.getLongExtra("dueDateMillis", 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "expense_channel_id")
                .setSmallIcon(R.drawable.rent_icon)
                .setContentTitle("Expense Due date")
                .setContentText("An expense is due today.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}
