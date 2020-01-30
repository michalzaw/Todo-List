package com.mich.todolist.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.mich.todolist.R;

import java.util.Random;

public class AppNotificationManager {

    private static final String CHANEL_ID = "TASK_CHANEL";

    private static AppNotificationManager instance;

    private Context context;

    private AppNotificationManager(Context context) {
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ToDoNotificationChanel";
            String description = "ToDoNotificationChanel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public static AppNotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new AppNotificationManager(context);
        }

        return instance;
    }

    public int showNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = generateNotifiactionId();
        notificationManager.notify(notificationId, builder.build());

        return notificationId;
    }

    private int generateNotifiactionId() {
        Random random = new Random();
        return random.nextInt();
    }
}
