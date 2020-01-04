package com.mich.todolist.database;

import android.content.Context;
import android.os.AsyncTask;

import com.mich.todolist.models.TaskEntity;

import java.util.concurrent.Callable;

public class TaskRepository {

    private final Context context;

    public TaskRepository(Context context) {
        this.context = context;
    }

    public void addTask(final TaskEntity task, Callable<Void> successCallback) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ApplicationDatabase.getInstance(context).taskDao().insert(task);

                try {
                    successCallback.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();
    }

    public void updateTask(final TaskEntity task) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ApplicationDatabase.getInstance(context).taskDao().update(task);

                return null;
            }
        }.execute();
    }
}
