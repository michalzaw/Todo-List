package com.mich.todolist.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mich.todolist.database.TaskRepository;
import com.mich.todolist.models.TaskEntity;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BROADCAST_RECEIVER" ,"Start");

        TaskRepository taskRepository = new TaskRepository(context);
        taskRepository.loadNotNotifiedTasks(tasks -> {
            for (TaskEntity task : tasks) {
                Calendar currentDate = Calendar.getInstance();
                Calendar taskDate = CalendarConverter.stringToCalendar(task.getDate(), CalendarConverter.DATE_AND_TIME_FORMAT);

                Log.d("BROADCAST_RECEIVER", task.getTitle() + " " + task.getUserNotified());
                if (taskDate.getTimeInMillis() - currentDate.getTimeInMillis() <= AppConstants.FIFTEEN_MINUTES_IN_MILIS && !task.getUserNotified()) {

                    AppNotificationManager.getInstance(context).showNotification(task.getTitle(), task.getDate());

                    task.setUserNotified(true);
                    taskRepository.updateTask(task);
                }
            }
        });

        Log.d("BROADCAST_RECEIVER" ,"End");
    }
}
