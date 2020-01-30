package com.mich.todolist.database;

import android.content.Context;
import android.os.AsyncTask;

import com.mich.todolist.models.TaskEntity;

import java.util.List;
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

    public void loadAllTasks(LoadTasksListObserver successCallback) {
        new AsyncTask<Void, Void, Void>() {

            private List<TaskEntity> tasks;

            @Override
            protected Void doInBackground(Void... voids) {
                tasks = ApplicationDatabase.getInstance(context).taskDao().getAllTasks();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                successCallback.onTasksLoaded(tasks);
            }
        }.execute();
    }

    public interface LoadTasksListObserver {
        void onTasksLoaded(List<TaskEntity> tasks);
    }
}
